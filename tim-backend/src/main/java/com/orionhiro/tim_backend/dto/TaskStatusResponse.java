package com.orionhiro.tim_backend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TaskStatusResponse {
    private final String status;
}
