package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class EntityNotFoundException extends CustomException{
    public EntityNotFoundException (String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
