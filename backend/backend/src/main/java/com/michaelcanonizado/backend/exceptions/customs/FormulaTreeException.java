package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class FormulaTreeException extends CustomException {
    public FormulaTreeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
