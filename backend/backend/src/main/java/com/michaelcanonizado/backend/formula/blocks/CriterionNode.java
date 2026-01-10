package com.michaelcanonizado.backend.formula.blocks;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.FormulaEvaluationException;
import com.michaelcanonizado.backend.exceptions.customs.FormulaInvalidWorkspaceException;
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
            throw new FormulaEvaluationException(
                    "Formula evaluation error! CriterionNode with value: \"" + id + "\" cannot be resolved! No score in evaluation context.",
                    ErrorCode.FORMULA_EVALUATION_ERROR
            );
        }

        return new NumberValue(score);
    }

    @Override
    public ValueType getType(TypeContext context) {
        return ValueType.NUMBER;
    }
}
