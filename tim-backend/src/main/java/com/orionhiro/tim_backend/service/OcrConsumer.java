package com.orionhiro.tim_backend.service;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OcrConsumer {
    private final TaskService taskService;

    @RabbitListener(queues="${queue.ocr.output}")
    public void receive(Map<String, String> message){
        taskService.updateTask(message);
    }
}
