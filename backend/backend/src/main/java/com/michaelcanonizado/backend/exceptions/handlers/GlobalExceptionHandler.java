package com.michaelcanonizado.backend.exceptions.handlers;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.common.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleRequestInvalidBodyException(HttpMessageNotReadableException exception, HttpServletRequest request) throws IOException {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ErrorCode.INVALID_REQUEST_BODY,
                exception.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<ErrorResponse> handlePropertyValueException(PropertyValueException exception, HttpServletRequest request) throws IOException {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String propertyName = exception.getPropertyName();
        String entityName = exception.getEntityName();
        String message = "Cannot persist property '" + propertyName + "' of entity '" + entityName +
                "': the value is either null for a non-optional field, or it references an unsaved transient instance.";

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ErrorCode.INVALID_REQUEST_BODY,
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }
}
