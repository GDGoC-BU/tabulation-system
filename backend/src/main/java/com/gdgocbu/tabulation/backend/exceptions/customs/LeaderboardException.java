package com.gdgocbu.tabulation.backend.exceptions.customs;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;

public class LeaderboardException extends CustomException {
    public LeaderboardException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
