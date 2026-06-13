package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class PageantContextMissingException extends CustomException {
    public PageantContextMissingException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
