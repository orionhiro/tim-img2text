package com.orionhiro.tim_backend.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.coyote.BadRequestException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.orionhiro.tim_backend.exception.TaskNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ImageService imageService;
    private final MessageService messageService;
    private final RedisTemplate<String, Object> redisTemplate;

    public Map<String, String> addTask(MultipartFile image) throws BadRequestException{
        
        String image_url = imageService.saveImage(image);
        String taskId = UUID.randomUUID().toString();

        redisTemplate.opsForHash().putAll(taskId, Map.of("status", "PENDING"));
        redisTemplate.expire(taskId, 60 * 60, TimeUnit.SECONDS);

        messageService.send(Map.of(
            "task_id", taskId,
            "image_url", image_url
        ));

        return Map.of(
            "task_id", taskId,
            "status", "PENDING"
        );
    }

    public Map<String, String> getTask(String id){
        Map<String, String> task = (Map) redisTemplate.opsForHash().entries(id);

        if(task == null || task.isEmpty()){
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }

        if(task.get("status").equals("FAILED") || task.get("status").equals("PENDING")){
            return Map.of("status", task.get("status"));
        }

        return Map.of("status", task.get("status"), "result", task.get("result"));
    }

    public void updateTask(Map<String, String> taskInfo){
        String taskId = taskInfo.get("task_id");

        redisTemplate.opsForHash().put(taskId, "status", taskInfo.get("status"));
        redisTemplate.opsForHash().put(taskId, "result", taskInfo.get("result"));
    }
}
