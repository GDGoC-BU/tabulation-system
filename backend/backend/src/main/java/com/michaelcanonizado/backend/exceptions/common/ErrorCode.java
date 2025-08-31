package com.michaelcanonizado.backend.exceptions.common;

public enum ErrorCode {
    ENTITY_NOT_FOUND,
    ENTITY_ALREADY_EXIST,

    SEGMENT_NOT_ACTIVE,
    PAGEANT_LOCKED,

    TOKEN_EXPIRED,
    TOKEN_INVALID,
    INVALID_CREDENTIALS,

    INTERNAL_ERROR,
    INVALID_REQUEST_BODY,
    DATABASE_ERROR
}