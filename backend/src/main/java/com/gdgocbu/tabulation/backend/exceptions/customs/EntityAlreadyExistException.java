package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class EntityAlreadyExistException extends CustomException {
    public EntityAlreadyExistException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
