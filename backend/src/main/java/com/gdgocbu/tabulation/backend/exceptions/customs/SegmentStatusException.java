package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class SegmentStatusException extends CustomException {
    public SegmentStatusException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
