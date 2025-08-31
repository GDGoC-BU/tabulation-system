package com.michaelcanonizado.backend.exceptions.handlers;

import com.michaelcanonizado.backend.exceptions.common.ErrorResponse;
import com.michaelcanonizado.backend.exceptions.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getErrorCode(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(EntityAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handleEntityAlreadyExistException(EntityAlreadyExistException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.CONFLICT;

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getErrorCode(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(EntityMismatchException.class)
    public ResponseEntity<ErrorResponse> handleEntityMismatchException(EntityMismatchException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getErrorCode(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(PageantStatusException.class)
    public ResponseEntity<ErrorResponse> handlePageantStatusException(PageantStatusException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.LOCKED;

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getErrorCode(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(SegmentStatusException.class)
    public ResponseEntity<ErrorResponse> handleSegmentStatusException(SegmentStatusException exception, HttpServletRequest request) {
        HttpStatus status = HttpStatus.FORBIDDEN;

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                exception.getErrorCode(),
                exception.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }
}
