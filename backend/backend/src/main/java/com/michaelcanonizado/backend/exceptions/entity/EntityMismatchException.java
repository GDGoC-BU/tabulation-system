package com.michaelcanonizado.backend.exceptions.entity;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class EntityMismatchException extends CustomException {
    public EntityMismatchException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
