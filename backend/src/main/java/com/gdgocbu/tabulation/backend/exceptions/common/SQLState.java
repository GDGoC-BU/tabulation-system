package com.gdgocbu.tabulation.backend.exceptions.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SQLState {
    private String value;
    private String message;
}
