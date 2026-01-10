package com.michaelcanonizado.backend.formula.contexts;

import com.michaelcanonizado.backend.formula.functions.FunctionRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.UUID;

@Component
public class FormulaContextFactory {
    @Autowired
    private FunctionRegistry functionRegistry;

    public EvaluationContext createEvaluationContext(
            MathContext mathContext,
            Map<UUID, BigDecimal> criteriaValues
    ) {
        return new EvaluationContext(
                mathContext,
                functionRegistry,
                criteriaValues
        );
    }

    public TypeContext createTypeContext() {
        return new TypeContext(functionRegistry);
    }
}
