package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class FormulaTypeException extends CustomException {
    public FormulaTypeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
