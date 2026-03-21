package com.orionhiro.tim_backend.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import io.minio.MinioClient;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "minio.bucket=test-bucket",
    "minio.url=test-url"
})
public class TaskServiceTest {
    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private TaskService taskService;

    @Test
    void testEmptyFile(){
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "image.png",
            "image/png",
            "".getBytes());

        assertThrows(BadRequestException.class, () -> taskService.addTask(file));

    }

    @Test
    void testNullFile(){
        assertThrows(BadRequestException.class, () -> taskService.addTask(null));
    }

    @Test
    void testIncorrectContentType(){
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "image.png",
            "application/json",
            "".getBytes());

        assertThrows(BadRequestException.class, () -> taskService.addTask(file));

    }

    @Test
    void testIncorrectFileExtension(){
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "image.exe",
            "image/png",
            "".getBytes());

        assertThrows(BadRequestException.class, () -> taskService.addTask(file));

    }

    @Test
    void testBucketNotFound(){
        try {
            
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.png",
                "image/png",
                "CONTENT".getBytes());

            taskService.addTask(file);

            verify(minioClient, times(1)).bucketExists(any());
            verify(minioClient, times(1)).putObject(any());
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }

}
