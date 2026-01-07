package com.michaelcanonizado.backend.formula.blocks;

import com.michaelcanonizado.GDGoCTabulationSandbox.formula.contexts.EvaluationContext;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.contexts.TypeContext;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.NumberValue;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.Value;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.ValueType;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
public class NumberLiteralNode implements BlockNode {
    private final BigDecimal value;

    @Override
    public Value evaluate(EvaluationContext context) {
        return new NumberValue(value);
    }

    @Override
    public ValueType getType(TypeContext context) {
        return ValueType.NUMBER;
    }
}
