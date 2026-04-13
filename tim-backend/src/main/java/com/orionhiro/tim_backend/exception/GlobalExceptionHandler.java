package com.orionhiro.tim_backend.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import io.netty.handler.codec.UnsupportedMessageTypeException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MediaUploadException.class)
    public ResponseEntity<Map<String, String>> handleMediaUploadException(Exception e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    "error", "Internal Server Error"
                ));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Map<String, String>> handleBadRequestException(Exception e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", String.valueOf(HttpStatus.BAD_REQUEST.value()),
                    "error", "Bad Request"
                ));
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxUploadSizeExceededException(Exception e){
        return ResponseEntity
                .status(HttpStatus.CONTENT_TOO_LARGE)
                .body(Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", String.valueOf(HttpStatus.CONTENT_TOO_LARGE.value()),
                    "error", "Payload Too Large"
                ));
    }

    @ExceptionHandler(UnsupportedMessageTypeException.class)
    public ResponseEntity<Map<String, String>> handleUnsupportedMessageTypeException(Exception e){
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()),
                    "error", "Unsupported Media Type"
                ));
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTaskNotFoundExceptionException(Exception e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                    "timestamp", LocalDateTime.now().toString(),
                    "status", String.valueOf(HttpStatus.NOT_FOUND.value()),
                    "error", e.getMessage()
                ));
    }
}
