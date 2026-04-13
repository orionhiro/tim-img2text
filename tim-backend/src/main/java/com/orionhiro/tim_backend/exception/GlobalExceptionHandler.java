package com.orionhiro.tim_backend.exception;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.orionhiro.tim_backend.dto.ErrorMessage;

import io.netty.handler.codec.UnsupportedMessageTypeException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MediaUploadException.class)
    public ResponseEntity<ErrorMessage> handleMediaUploadException(Exception e){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorMessage(
                    LocalDateTime.now(), 
                    String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), 
                    "Internal Server Error")
                );
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorMessage> handleBadRequestException(Exception e){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorMessage(
                    LocalDateTime.now(), 
                    String.valueOf(HttpStatus.BAD_REQUEST.value()), 
                    "Bad Request")
                );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorMessage> handleMaxUploadSizeExceededException(Exception e){
        return ResponseEntity
                .status(HttpStatus.CONTENT_TOO_LARGE)
                .body(new ErrorMessage(
                    LocalDateTime.now(), 
                    String.valueOf(HttpStatus.CONTENT_TOO_LARGE.value()), 
                    "Payload Too Large")
                );
    }

    @ExceptionHandler(UnsupportedMessageTypeException.class)
    public ResponseEntity<ErrorMessage> handleUnsupportedMessageTypeException(Exception e){
        return ResponseEntity
                .status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErrorMessage(
                    LocalDateTime.now(), 
                    String.valueOf(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value()), 
                    "Unsupported Media Type")
                );
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleTaskNotFoundExceptionException(Exception e){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(
                    LocalDateTime.now(), 
                    String.valueOf(HttpStatus.NOT_FOUND.value()), 
                    e.getMessage())
                );
    }
}
