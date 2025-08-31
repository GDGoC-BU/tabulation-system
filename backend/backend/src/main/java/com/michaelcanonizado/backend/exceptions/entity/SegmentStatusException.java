package com.michaelcanonizado.backend.exceptions.entity;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class SegmentStatusException extends CustomException {
    public SegmentStatusException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
