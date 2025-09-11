package com.reddit.coreService.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorInfo {
    private final ErrorCode errorCode;
    private final int status;
    private final String message;
    private final LocalDateTime timestamp;

    public ErrorInfo(ErrorCode errorCode, int status, String message) {
        this.errorCode = errorCode;
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }
}

