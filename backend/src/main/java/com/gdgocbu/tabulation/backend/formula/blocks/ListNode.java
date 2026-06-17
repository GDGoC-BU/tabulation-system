package com.gdgocbu.tabulation.backend.formula.blocks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;
import com.gdgocbu.tabulation.backend.exceptions.customs.FormulaEvaluationException;
import com.gdgocbu.tabulation.backend.exceptions.customs.FormulaTypeException;
import com.gdgocbu.tabulation.backend.formula.contexts.EvaluationContext;
import com.gdgocbu.tabulation.backend.formula.contexts.TypeContext;
import com.gdgocbu.tabulation.backend.formula.values.NumberListValue;
import com.gdgocbu.tabulation.backend.formula.values.NumberValue;
import com.gdgocbu.tabulation.backend.formula.values.Value;
import com.gdgocbu.tabulation.backend.formula.values.ValueType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
public final class ListNode implements BlockNode {
    private final List<BlockNode> elements;

    @JsonCreator
    public ListNode(
            @JsonProperty("elements") List<BlockNode> elements
    ) {
        this.elements = elements;
    }

    @Override
    public Value evaluate(EvaluationContext context) {
        List<BigDecimal> values = new ArrayList<>();

        /* Since the list can contain any BlockNode, evaluate
        * each node to get the actual value of the element. */
        for (BlockNode element : elements) {
            Value value = element.evaluate(context);

            /* Check the type of each element */
            if (value instanceof NumberValue(BigDecimal value1)) {
                values.add(value1);
            } else {
                throw new FormulaEvaluationException(
                        "Formula evaluation error! Invalid value: " + value +". ListNode only accepts NumberValue",
                        ErrorCode.FORMULA_EVALUATION_ERROR
                );
            }
        }

        return new NumberListValue(values);
    }

    @Override
    public ValueType getType(TypeContext context) {
        for (BlockNode element : elements) {
            ValueType elementType = element.getType(context);

            if (elementType != ValueType.NUMBER) {
                throw new FormulaTypeException(
                        "Invalid formula! ListNode only accepts: [ Number ]",
                        ErrorCode.FORMULA_TYPE_ERROR
                );
            }
        }

        return ValueType.NUMBER_LIST;
    }
}
