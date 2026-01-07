package com.michaelcanonizado.backend.formula.contexts;

import com.michaelcanonizado.GDGoCTabulationSandbox.formula.functions.FunctionRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TypeContext {
    private final FunctionRegistry functionRegistry;
}
