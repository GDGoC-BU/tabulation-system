package com.gdgocbu.tabulation.backend.formula.blocks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;
import com.gdgocbu.tabulation.backend.exceptions.customs.FormulaEvaluationException;
import com.gdgocbu.tabulation.backend.exceptions.customs.FormulaTypeException;
import com.gdgocbu.tabulation.backend.formula.contexts.EvaluationContext;
import com.gdgocbu.tabulation.backend.formula.contexts.TypeContext;
import com.gdgocbu.tabulation.backend.formula.values.NumberValue;
import com.gdgocbu.tabulation.backend.formula.values.Value;
import com.gdgocbu.tabulation.backend.formula.values.ValueType;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
public final class BinaryOperationNode implements BlockNode {
    private final BlockNode left;
    private final BinaryOperator operator;
    private final BlockNode right;

    @JsonCreator
    public BinaryOperationNode(
            @JsonProperty("left") BlockNode left,
            @JsonProperty("operator") BinaryOperator operator,
            @JsonProperty("right") BlockNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    @Override
    public Value evaluate(EvaluationContext context) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
        Objects.requireNonNull(operator, "operator");

        /* Recursively evaluate their inputs */
        NumberValue leftValue = (NumberValue) left.evaluate(context);
        NumberValue rightValue = (NumberValue) right.evaluate(context);

        BigDecimal leftBigDecimal = leftValue.value();
        BigDecimal rightBigDecimal = rightValue.value();

        /* Perform operation */
        BigDecimal result = switch (operator) {
            case ADD -> leftBigDecimal.add(rightBigDecimal);
            case SUBTRACT -> leftBigDecimal.subtract(rightBigDecimal);
            case MULTIPLY -> leftBigDecimal.multiply(rightBigDecimal);
            case DIVIDE -> leftBigDecimal.divide(rightBigDecimal, context.getMathContext());
            default -> {
                throw new FormulaEvaluationException(
                        "Formula evaluation error! BinaryOperationNode can't determine action for operator: " + operator.toString(),
                        ErrorCode.FORMULA_EVALUATION_ERROR
                );
            }
        };

        return new NumberValue(result);
    }

    @Override
    public ValueType getType(TypeContext context) {
        Objects.requireNonNull(left, "left");
        Objects.requireNonNull(right, "right");
        Objects.requireNonNull(operator, "operator");

        /* Recursively verify the type of inputs */
        ValueType leftType = left.getType(context);
        ValueType rightType = right.getType(context);


        /* Inputs must be a NUMBER */
        if (
                leftType != ValueType.NUMBER ||
                rightType != ValueType.NUMBER
        ) {
            throw new FormulaTypeException(
                    "Invalid formula! BinaryOperationNode left and right only accept: [ Number ]",
                    ErrorCode.FORMULA_TYPE_ERROR
            );
        }

        return ValueType.NUMBER;
    }
}
