package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class AuthenticationFailedException extends CustomException {
    public AuthenticationFailedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
