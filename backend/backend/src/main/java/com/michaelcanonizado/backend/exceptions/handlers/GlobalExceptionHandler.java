package com.michaelcanonizado.backend.exceptions.handlers;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.common.ErrorResponse;
import com.michaelcanonizado.backend.exceptions.common.SQLState;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String message = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ErrorCode.INVALID_REQUEST_BODY,
                message,
                request.getRequestURI()
        );

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException exception,
            HttpServletRequest request
    ) throws IOException {
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
    public ResponseEntity<ErrorResponse> handlePropertyValueException(
            PropertyValueException exception,
            HttpServletRequest request
    ) throws IOException {
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

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(
            ConstraintViolationException exception,
            HttpServletRequest request
    ) throws IOException {
        HttpStatus status = HttpStatus.BAD_REQUEST;

        String sqlState = exception.getSQLException().getSQLState();

        /* More scalable way to handle sql errors.

           Reference: https://en.wikipedia.org/wiki/SQLSTATE

           Add more attributes to SQLState class when
           improving this, to match the table.
           Probably even add an ErrorCode attribute also.*/
        List<SQLState> sqlStates = Arrays.asList(
                new SQLState("23505", "Duplicate value detected! A unique constraint was violated while trying to persist. Please ensure that fields marked as unique do not contain duplicates."),
                new SQLState("23503", "Foreign key constraint violated. Referenced entity may not exist.")
        );

        String message = sqlStates.stream().filter(s -> s.getValue().equals(sqlState)).map(SQLState::getMessage).findFirst().orElse("Database constraint violation!");


        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ErrorCode.DATABASE_ERROR,
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwtException(
            ExpiredJwtException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = "Token expired! Please login again.";

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ErrorCode.TOKEN_EXPIRED,
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(
            BadCredentialsException exception,
            HttpServletRequest request
    ) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        String message = "Wrong credentials! Please try again.";

        ErrorResponse response = new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                ErrorCode.INVALID_CREDENTIALS,
                message,
                request.getRequestURI()
        );
        return new ResponseEntity<>(response, status);
    }
}
