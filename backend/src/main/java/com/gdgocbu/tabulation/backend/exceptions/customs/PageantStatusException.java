package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class PageantStatusException extends CustomException {
    public PageantStatusException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
