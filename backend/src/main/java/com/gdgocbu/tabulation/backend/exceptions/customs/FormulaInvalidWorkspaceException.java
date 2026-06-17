package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class FormulaInvalidWorkspaceException extends CustomException {
    public FormulaInvalidWorkspaceException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
