package com.michaelcanonizado.backend.exceptions.entity;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class EntityNotFoundException extends CustomException{
    public EntityNotFoundException (String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
