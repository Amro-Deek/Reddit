package com.reddit.coreService.exceptions;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorInfo> handleAllExceptions(Exception ex) {
        ErrorInfo error = new ErrorInfo(ErrorCode.INTERNAL_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Unexpected error occurred"
                );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
    //  Handle Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorInfo> handleNotFound(ResourceNotFoundException ex) {
        ErrorInfo error = new ErrorInfo(
                ex.getErrorCode(),
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    //  Handle Conflict
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorInfo> handleConflict(ConflictException ex) {
        ErrorInfo error = new ErrorInfo(
                ex.getErrorCode(),
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    //  Handle Unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorInfo> handleUnauthorized(UnauthorizedException ex) {
        ErrorInfo error = new ErrorInfo(
                ex.getErrorCode(),
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    //  Handle Forbidden
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorInfo> handleForbidden(ForbiddenException ex) {
        ErrorInfo error = new ErrorInfo(
                ex.getErrorCode(),
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    // Invalid input (bad format, wrong params, etc.)
    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<ErrorInfo> handleInvalidInput(InvalidInputException ex) {
        ErrorInfo error = new ErrorInfo(
                ErrorCode.INVALID_INPUT,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Validation errors (business rule / constraint violations)
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorInfo> handleValidation(ValidationException ex) {
        ErrorInfo error = new ErrorInfo(
                ErrorCode.VALIDATION_ERROR,
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    // Handle validation errors from @Valid on @RequestBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorInfo> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorInfo error = new ErrorInfo(
                ErrorCode.VALIDATION_ERROR,
                HttpStatus.BAD_REQUEST.value(),
                errorMessages
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Handle validation errors for @RequestParam, @PathVariable (ConstraintViolation)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorInfo> handleConstraintViolation(ConstraintViolationException ex) {
        String errorMessages = ex.getConstraintViolations()
                .stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));

        ErrorInfo error = new ErrorInfo(
                ErrorCode.VALIDATION_ERROR,
                HttpStatus.BAD_REQUEST.value(),
                errorMessages
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Handle invalid JSON body or wrong field type
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorInfo> handleInvalidFormat(HttpMessageNotReadableException ex) {
        ErrorInfo error = new ErrorInfo(
                ErrorCode.INVALID_INPUT,
                HttpStatus.BAD_REQUEST.value(),
                "Invalid request body or field format"
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Handle wrong type in query params/path variables
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorInfo> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String msg = String.format("Invalid value for parameter '%s': expected type %s",
                ex.getName(), ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown");

        ErrorInfo error = new ErrorInfo(
                ErrorCode.INVALID_INPUT,
                HttpStatus.BAD_REQUEST.value(),
                msg
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


}
