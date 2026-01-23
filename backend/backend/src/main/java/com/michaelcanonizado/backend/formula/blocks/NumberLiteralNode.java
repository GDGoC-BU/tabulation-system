package com.michaelcanonizado.backend.formula.blocks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.michaelcanonizado.backend.formula.contexts.EvaluationContext;
import com.michaelcanonizado.backend.formula.contexts.TypeContext;
import com.michaelcanonizado.backend.formula.values.NumberValue;
import com.michaelcanonizado.backend.formula.values.Value;
import com.michaelcanonizado.backend.formula.values.ValueType;
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
