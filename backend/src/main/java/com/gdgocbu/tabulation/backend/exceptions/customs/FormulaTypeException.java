package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class FormulaTypeException extends CustomException {
    public FormulaTypeException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
