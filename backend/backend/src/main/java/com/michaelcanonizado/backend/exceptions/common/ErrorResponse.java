package com.michaelcanonizado.backend.exceptions.common;

import lombok.Getter;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Getter
public class ErrorResponse {
    private int status;
    private String error;
    private Code code;
    private String message;
    private String path;
    private ZonedDateTime timestamp;

    public ErrorResponse(int status, String error, Code code, String message, String path) {
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.path = path;
        this.timestamp = ZonedDateTime.now(ZoneId.of("Asia/Manila"));
    }
}
