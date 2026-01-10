package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class FormulaInvalidWorkspaceException extends CustomException {
    public FormulaInvalidWorkspaceException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
