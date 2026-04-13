package com.orionhiro.tim_backend.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SuccessTaskResponse {
    private final String status;
    private final String result;
}
