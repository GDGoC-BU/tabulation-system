package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class UnauthorizedWebsocketConnection extends CustomException {
    public UnauthorizedWebsocketConnection(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
