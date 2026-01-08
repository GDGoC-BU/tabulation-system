package com.michaelcanonizado.backend.formula.blocks;

import com.michaelcanonizado.backend.formula.contexts.EvaluationContext;
import com.michaelcanonizado.backend.formula.contexts.TypeContext;
import com.michaelcanonizado.backend.formula.values.NumberValue;
import com.michaelcanonizado.backend.formula.values.Value;
import com.michaelcanonizado.backend.formula.values.ValueType;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
public class CriterionNode implements BlockNode {
    private final UUID id;

    @Override
    public Value evaluate(EvaluationContext context) {
        BigDecimal score = context.getCriteriaValues().get(id);

        if (score == null) {
//            score is not in the criterionScore map
        }

        return new NumberValue(score);
    }

    @Override
    public ValueType getType(TypeContext context) {
        return ValueType.NUMBER;
    }
}
