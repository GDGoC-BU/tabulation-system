package com.michaelcanonizado.backend.formula.contexts;

import com.michaelcanonizado.backend.formula.functions.FunctionRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TypeContext {
    private final FunctionRegistry functionRegistry;
}
