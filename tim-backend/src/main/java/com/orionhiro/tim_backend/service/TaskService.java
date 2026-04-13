package com.orionhiro.tim_backend.service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.coyote.BadRequestException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.orionhiro.tim_backend.dto.NewTaskResponse;
import com.orionhiro.tim_backend.dto.SuccessTaskResponse;
import com.orionhiro.tim_backend.dto.TaskMessage;
import com.orionhiro.tim_backend.dto.TaskStatusResponse;
import com.orionhiro.tim_backend.exception.TaskNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final ImageService imageService;
    private final MessageService messageService;
    private final RedisTemplate<String, Object> redisTemplate;

    public NewTaskResponse addTask(MultipartFile image) throws BadRequestException{
        
        String image_url = imageService.saveImage(image);
        String taskId = UUID.randomUUID().toString();

        redisTemplate.opsForHash().putAll(taskId, Map.of("status", "PENDING"));
        redisTemplate.expire(taskId, 60 * 60, TimeUnit.SECONDS);

        messageService.send(new TaskMessage(taskId, image_url));

        return new NewTaskResponse(taskId, "PENDING");
    }

    public Object getTask(String id){
        Map<String, String> task = (Map) redisTemplate.opsForHash().entries(id);

        if(task == null || task.isEmpty()){
            throw new TaskNotFoundException("Task with id " + id + " not found");
        }

        if(task.get("status").equals("FAILED") || task.get("status").equals("PENDING")){
            return new TaskStatusResponse(task.get("status"));
        }

        return new SuccessTaskResponse(task.get("status"), task.get("result"));
    }

    public void updateTask(Map<String, String> taskInfo){
        String taskId = taskInfo.get("taskId");

        redisTemplate.opsForHash().put(taskId, "status", taskInfo.get("status"));
        redisTemplate.opsForHash().put(taskId, "result", taskInfo.get("result"));
    }
}
