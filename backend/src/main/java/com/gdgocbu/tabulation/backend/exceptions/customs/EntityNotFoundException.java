package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class EntityNotFoundException extends CustomException{
    public EntityNotFoundException (String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
