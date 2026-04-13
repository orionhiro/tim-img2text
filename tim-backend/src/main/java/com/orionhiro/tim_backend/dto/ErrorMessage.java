package com.orionhiro.tim_backend.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ErrorMessage {
    private final LocalDateTime timestamp;
    private final String status;
    private final String error;
}
