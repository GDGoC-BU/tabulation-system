package com.michaelcanonizado.backend.exceptions.customs;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;

public class LeaderboardException extends CustomException {
    public LeaderboardException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
