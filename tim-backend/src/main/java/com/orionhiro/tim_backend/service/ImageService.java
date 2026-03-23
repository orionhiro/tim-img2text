package com.orionhiro.tim_backend.service;

import java.io.InputStream;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.orionhiro.tim_backend.exception.MediaUploadException;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.netty.handler.codec.UnsupportedMessageTypeException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;
    @Value("${minio.url}")
    private String minioUrl;

    public String saveImage(MultipartFile image) throws BadRequestException{
        if(image == null || image.isEmpty()){
            throw new BadRequestException();
        }

        if(!checkIsImage(image)){
            throw new UnsupportedMessageTypeException();
        }

        String image_url = null;

        try{
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }

            try (InputStream is = image.getInputStream()) {
                image_url = UUID.randomUUID().toString() + "." + image.getOriginalFilename().split("\\.")[1];
                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(image_url)
                        .stream(is, is.available(), -1)
                        .contentType(image.getContentType())
                        .build()
                );
            }
        } catch(Exception exc){
            throw new MediaUploadException(exc.getMessage());
        }

        return image_url;
    }

    private boolean checkIsImage(MultipartFile file){
        boolean checkContentType = file.getContentType() != null &&
                  file.getContentType().startsWith("image/");
        
        String name = file.getOriginalFilename();
        boolean checkFileExtension = name != null && name.matches(".*\\.(png|jpg|jpeg|gif|bmp)$");

        return checkContentType && checkFileExtension;
    }
}
