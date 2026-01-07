package com.michaelcanonizado.backend.formula.contexts;

import com.michaelcanonizado.backend.formula.functions.FunctionRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.MathContext;

@AllArgsConstructor
@Getter
public class EvaluationContext {
    private final MathContext mathContext;
    private final FunctionRegistry functionRegistry;
}
