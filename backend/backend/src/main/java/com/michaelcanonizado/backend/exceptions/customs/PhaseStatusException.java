package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class PhaseStatusException extends CustomException{
    public PhaseStatusException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
