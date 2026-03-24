package com.orionhiro.tim_backend.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
            doNothing().when(messageService).send(Mockito.anyMap());

            taskService.addTask(new MockMultipartFile("file_to_save.png", "content".getBytes()));
        } catch (BadRequestException e) {
            e.printStackTrace();
        }
        
    }

}
