package com.reddit.coreService.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // 403

public class ForbiddenException extends CoreException{

    public ForbiddenException(String message) {
        super(message, ErrorCode.FORBIDDEN);
    }
}