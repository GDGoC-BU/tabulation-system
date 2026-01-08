package com.michaelcanonizado.backend.formula.contexts;

import com.michaelcanonizado.backend.formula.functions.FunctionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.MathContext;

@Component
public class FormulaContextFactory {
    @Autowired
    private FunctionRegistry functionRegistry;

    public EvaluationContext createEvaluationContext(
            MathContext mathContext
    ) {
        return new EvaluationContext(
                mathContext,
                functionRegistry
        );
    }

    public TypeContext createTypeContext() {
        return new TypeContext(functionRegistry);
    }
}
