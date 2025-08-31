package com.michaelcanonizado.backend.exceptions.common;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public class ErrorResponse {
    /* HTTP status code. E.g: 404, 200, 500 */
    private final int status;

    /* HTTP status phrase: E.g: Not Found, Ok, Internal Server Error */
    private final String statusPhrase;

    /* Machine-readable identifier for an error.
       Can be used to programmatically handle the errors, especially in the
       frontend. */
    private final ErrorCode errorCode;

    /* More detailed message of the error. Passed by the Caller */
    private final String message;

    /* URI where the error happened */
    private final String path;

    /* When the error occurred */
    private final LocalDateTime timestamp;

    public ErrorResponse(int status, String statusPhrase, ErrorCode errorCode, String message, String path) {
        this.status = status;
        this.statusPhrase = statusPhrase;
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }
}
