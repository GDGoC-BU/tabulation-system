package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class UnsupportedAccountTypeException extends CustomException {
    public UnsupportedAccountTypeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
