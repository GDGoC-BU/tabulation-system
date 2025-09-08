package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class PageantAccessDeniedException extends CustomException {
    public PageantAccessDeniedException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
