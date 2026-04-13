package com.orionhiro.tim_backend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NewTaskResponse {
    private final String taskId;
    private final String status;
}
