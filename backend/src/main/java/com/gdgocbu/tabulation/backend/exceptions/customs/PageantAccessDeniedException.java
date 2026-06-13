package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class PageantAccessDeniedException extends CustomException {
    public PageantAccessDeniedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
