package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class PageantContextMissingException extends CustomException {
    public PageantContextMissingException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
