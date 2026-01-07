package com.michaelcanonizado.backend.formula.blocks;

import com.michaelcanonizado.GDGoCTabulationSandbox.formula.contexts.EvaluationContext;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.contexts.TypeContext;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.NumberValue;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.Value;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.ValueType;
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
            throw new IllegalStateException(
                    "BinaryOperationNode require inputs as NUMBER"
            );
        }

        return ValueType.NUMBER;
    }
}
