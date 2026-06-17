package com.gdgocbu.tabulation.backend.formula.blocks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;
import com.gdgocbu.tabulation.backend.exceptions.customs.FormulaEvaluationException;
import com.gdgocbu.tabulation.backend.formula.contexts.EvaluationContext;
import com.gdgocbu.tabulation.backend.formula.contexts.TypeContext;
import com.gdgocbu.tabulation.backend.formula.values.NumberValue;
import com.gdgocbu.tabulation.backend.formula.values.Value;
import com.gdgocbu.tabulation.backend.formula.values.ValueType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
public final class CriterionNode implements BlockNode {
    private final UUID id;

    @JsonCreator
    public CriterionNode(
            @JsonProperty("id") UUID id
    ) {
        this.id = id;
    }

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
