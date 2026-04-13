package com.orionhiro.tim_backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import com.orionhiro.tim_backend.dto.SuccessTaskResponse;
import com.orionhiro.tim_backend.dto.TaskMessage;
import com.orionhiro.tim_backend.dto.TaskStatusResponse;
import com.orionhiro.tim_backend.exception.TaskNotFoundException;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "minio.bucket=test-bucket",
    "minio.url=test-url"
})
public class TaskServiceTest {
    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    @Mock
    private ImageService imageService;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testAddTask(){
        try {
            when(imageService.saveImage(Mockito.any())).thenReturn("saved_file.png");
            when(redisTemplate.opsForHash()).thenReturn(hashOperations);
            doNothing().when(hashOperations).putAll(Mockito.anyString(), Mockito.anyMap());
            when(redisTemplate.expire(Mockito.anyString(), Mockito.anyLong(), Mockito.any(TimeUnit.class))).thenReturn(true);
            doNothing().when(messageService).send(Mockito.any(TaskMessage.class));

            var result = taskService.addTask(new MockMultipartFile("file_to_save.png", "content".getBytes()));

            assertNotNull(result.getTaskId());
            assertEquals("PENDING", result.getStatus());
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        
    }

    @Test
    void testGetTask(){
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.entries(Mockito.anyString())).thenReturn(Map.of("status", "SUCCESS", "result", "some text"));

        var result = (SuccessTaskResponse) taskService.getTask("0000");

        assertEquals("SUCCESS", result.getStatus());
        assertEquals("some text", result.getResult());
    }

    @Test
    void testGetNotSuccessTask(){
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.entries(Mockito.anyString())).thenReturn(Map.of("status", "FAILED"));

        var result = (TaskStatusResponse) taskService.getTask("0000");

        assertEquals("FAILED", result.getStatus());
    }

        @Test
    void testNotFoundTask(){
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
        when(hashOperations.entries(Mockito.anyString())).thenReturn(Map.of());

        assertThrows(TaskNotFoundException.class, () -> taskService.getTask("0"));
    }

}
