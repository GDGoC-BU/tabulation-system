package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class UnsupportedAccountTypeException extends CustomException {
    public UnsupportedAccountTypeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
