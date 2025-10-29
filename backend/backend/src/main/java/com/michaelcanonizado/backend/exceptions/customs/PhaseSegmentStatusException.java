package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class PhaseSegmentStatusException extends CustomException{
    public PhaseSegmentStatusException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
