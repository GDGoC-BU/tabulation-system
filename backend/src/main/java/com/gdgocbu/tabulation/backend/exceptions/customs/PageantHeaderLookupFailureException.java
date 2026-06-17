package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class PageantHeaderLookupFailureException extends CustomException {
    public PageantHeaderLookupFailureException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
