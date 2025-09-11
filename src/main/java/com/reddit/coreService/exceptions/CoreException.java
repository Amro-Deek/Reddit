package com.reddit.coreService.exceptions;

public class CoreException extends RuntimeException{
    private final ErrorCode errorCode;

    public CoreException(String message, ErrorCode code) {
        super(message);
        this.errorCode = code;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
