package com.michaelcanonizado.backend.formula.blocks;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.FormulaEvaluationException;
import com.michaelcanonizado.backend.exceptions.customs.FormulaTypeException;
import com.michaelcanonizado.backend.formula.contexts.EvaluationContext;
import com.michaelcanonizado.backend.formula.contexts.TypeContext;
import com.michaelcanonizado.backend.formula.values.NumberValue;
import com.michaelcanonizado.backend.formula.values.Value;
import com.michaelcanonizado.backend.formula.values.ValueType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class BinaryOperationNode implements BlockNode {
    private final BlockNode left;
    private final BinaryOperator operator;
    private final BlockNode right;

    @Override
    public Value evaluate(EvaluationContext context) {
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
