package com.gdgocbu.tabulation.backend.formula.blocks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gdgocbu.tabulation.backend.formula.contexts.EvaluationContext;
import com.gdgocbu.tabulation.backend.formula.contexts.TypeContext;
import com.gdgocbu.tabulation.backend.formula.values.NumberValue;
import com.gdgocbu.tabulation.backend.formula.values.Value;
import com.gdgocbu.tabulation.backend.formula.values.ValueType;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public final class NumberLiteralNode implements BlockNode {
    private final BigDecimal value;

    @JsonCreator
    public NumberLiteralNode(
            @JsonProperty("value") BigDecimal value
    ) {
        this.value = value;
    }

    @Override
    public Value evaluate(EvaluationContext context) {
        return new NumberValue(value);
    }

    @Override
    public ValueType getType(TypeContext context) {
        return ValueType.NUMBER;
    }
}
