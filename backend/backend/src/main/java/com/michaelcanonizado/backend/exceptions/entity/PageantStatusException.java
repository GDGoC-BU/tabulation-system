package com.michaelcanonizado.backend.exceptions.entity;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class PageantStatusException extends CustomException {
    public PageantStatusException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
