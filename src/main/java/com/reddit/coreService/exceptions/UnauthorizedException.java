package com.reddit.coreService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED) // 401

public class UnauthorizedException extends CoreException{

    public UnauthorizedException(String message) {
        super(message, ErrorCode.UNAUTHORIZED);
    }
}