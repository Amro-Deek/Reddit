package com.reddit.coreService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // 401

public class ValidationException extends CoreException{

    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_ERROR);
    }
}