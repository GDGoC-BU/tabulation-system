package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class FormulaEvaluationException extends CustomException {
    public FormulaEvaluationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
