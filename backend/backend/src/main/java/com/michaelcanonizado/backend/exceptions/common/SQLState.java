package com.michaelcanonizado.backend.exceptions.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SQLState {
    private String value;
    private String message;
}
