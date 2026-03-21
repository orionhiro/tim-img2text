package com.orionhiro.tim_backend.controller;

import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.orionhiro.tim_backend.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> addTask(@RequestParam("image") MultipartFile image) throws BadRequestException{
        return taskService.addTask(image);
    }
}
