package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class FormulaEvaluationException extends CustomException {
    public FormulaEvaluationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
