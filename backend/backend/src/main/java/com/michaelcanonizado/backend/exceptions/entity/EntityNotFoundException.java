package com.michaelcanonizado.backend.exceptions.entity;

import com.michaelcanonizado.backend.exceptions.common.Code;

public class EntityNotFoundException extends CustomException{
    public EntityNotFoundException (String message, Code code) {
        super(message, code);
    }
}
