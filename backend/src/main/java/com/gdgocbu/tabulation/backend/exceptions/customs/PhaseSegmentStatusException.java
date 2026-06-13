package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class PhaseSegmentStatusException extends CustomException{
    public PhaseSegmentStatusException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
