package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class UnauthorizedWebsocketConnection extends CustomException {
    public UnauthorizedWebsocketConnection(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
