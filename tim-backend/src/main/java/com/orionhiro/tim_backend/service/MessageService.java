package com.orionhiro.tim_backend.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

    @Value("${queue.ocr.input}")
    private String queueInput;

    private final RabbitTemplate rabbitTemplate;

    public void send(Object message){
        rabbitTemplate.convertAndSend(queueInput, message);
    }
}
