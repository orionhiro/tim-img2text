package com.orionhiro.tim_backend.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.coyote.BadRequestException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ImageService imageService;
    private final RedisTemplate<String, Object> redisTemplate;

    public Map<String, String> addTask(MultipartFile image) throws BadRequestException{
        
        String image_url = imageService.saveImage(image);
        String taskId = UUID.randomUUID().toString();

        redisTemplate.opsForHash().putAll(taskId, Map.of("status", "PENDING"));
        redisTemplate.expire(taskId, 60 * 60, TimeUnit.SECONDS);

        return Map.of(
            "task_id", taskId,
            "status", "PENDING"
        );
    }
}
