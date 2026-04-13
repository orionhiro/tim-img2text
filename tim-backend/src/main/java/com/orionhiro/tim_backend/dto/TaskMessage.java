package com.orionhiro.tim_backend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TaskMessage {
    private final String taskId;
    private final String imageUrl;
}
