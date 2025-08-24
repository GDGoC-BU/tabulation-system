package com.michaelcanonizado.backend.exceptions.entity;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class EntityAlreadyExistException extends CustomException {
    public EntityAlreadyExistException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
