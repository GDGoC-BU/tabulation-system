package com.michaelcanonizado.backend.exceptions.entity;

import com.michaelcanonizado.backend.exceptions.common.Code;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private Code code;

    public CustomException (String message, Code code) {
        super(message);
        this.code = code;
    }
}
