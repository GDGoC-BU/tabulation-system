package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class PageantHeaderLookupFailureException extends CustomException {
    public PageantHeaderLookupFailureException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
