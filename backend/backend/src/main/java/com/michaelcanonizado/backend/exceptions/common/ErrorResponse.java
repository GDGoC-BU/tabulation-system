package com.michaelcanonizado.backend.exceptions.common;

import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public class ErrorResponse {
    /* HTTP status code. E.g: 404, 200, 500 */
    private int status;

    /* HTTP status phrase: E.g: Not Found, Ok, Internal Server Error */
    private String statusPhrase;

    /* Machine-readable identifier for an error.
       Can be used to programmatically handle the errors, especially in the
       frontend. Also since the entities have a generic custom exception per
       error type, this can be used to identify exactly what entity threw the
       error.
       E.g: COLLEGE_NOT_FOUND, CANDIDATE_ALREADY_EXIST, INTERNAL_ERROR */
    private ErrorCode errorCode;

    /* More detailed message of the error. Passed by the Caller */
    private String message;

    /* URI where the error happened */
    private String path;

    /* When the error occurred */
    private ZonedDateTime timestamp;

    public ErrorResponse(int status, String statusPhrase, ErrorCode errorCode, String message, String path) {
        this.status = status;
        this.statusPhrase = statusPhrase;
        this.errorCode = errorCode;
        this.message = message;
        this.path = path;
        this.timestamp = ZonedDateTime.now(ZoneId.of("Asia/Manila"));
    }
}
