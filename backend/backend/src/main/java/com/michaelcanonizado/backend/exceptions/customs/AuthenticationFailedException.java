package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class AuthenticationFailedException extends CustomException {
    public AuthenticationFailedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
