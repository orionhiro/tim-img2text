package com.orionhiro.tim_backend.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;

import com.orionhiro.tim_backend.exception.MediaUploadException;

import io.minio.MinioClient;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
    "minio.bucket=test-bucket",
    "minio.url=test-url"
})
public class ImageServiceTest {
    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private ImageService imageService;

    @Test
    void testEmptyFile(){
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "image.png",
            "image/png",
            "".getBytes());

        assertThrows(BadRequestException.class, () -> imageService.saveImage(file));

    }

    @Test
    void testNullFile(){
        assertThrows(BadRequestException.class, () -> imageService.saveImage(null));
    }

    @Test
    void testIncorrectContentType(){
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "image.png",
            "application/json",
            "".getBytes());

        assertThrows(BadRequestException.class, () -> imageService.saveImage(file));

    }

    @Test
    void testIncorrectFileExtension(){
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "image.exe",
            "image/png",
            "".getBytes());

        assertThrows(BadRequestException.class, () -> imageService.saveImage(file));

    }

    @Test
    void testBucketNotFound(){
        try {
            
            MockMultipartFile file = new MockMultipartFile(
                "file",
                "image.png",
                "image/png",
                "CONTENT".getBytes());

            assertThrows(MediaUploadException.class, () -> imageService.saveImage(file));
            
        } catch (Exception e) {
            e.printStackTrace();
        } 
    }
}
