package com.michaelcanonizado.backend.formula.functions;

import com.michaelcanonizado.backend.formula.blocks.BlockNode;
import com.michaelcanonizado.backend.formula.contexts.EvaluationContext;
import com.michaelcanonizado.backend.formula.contexts.TypeContext;
import com.michaelcanonizado.backend.formula.values.NumberListValue;
import com.michaelcanonizado.backend.formula.values.NumberValue;
import com.michaelcanonizado.backend.formula.values.Value;
import com.michaelcanonizado.backend.formula.values.ValueType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AverageFunction implements FunctionDefinition {
    @Override
    public Value evaluate(List<BlockNode> arguments, EvaluationContext context) {
        List<BigDecimal> values = new ArrayList<>();

        for (BlockNode argument : arguments) {
            Value value = argument.evaluate(context);

            if (value instanceof NumberValue numberValue) {
                values.add(numberValue.value());
            } else if (value instanceof  NumberListValue numberListValue) {
                values.addAll(numberListValue.values());
            } else {
                throw new IllegalStateException("Invalid avg argument");
            }
        }

        BigDecimal sum = BigDecimal.ZERO;

        for (BigDecimal value : values) {
            sum = sum.add(value);
        }

        return new NumberValue(
                sum.divide(
                        BigDecimal.valueOf(values.size()),
                        context.getMathContext()
                )
        );
    }

    @Override
    public ValueType getReturnType(List<BlockNode> arguments, TypeContext context) {
        if (arguments.isEmpty()) {
//            throw new IllegalStateException("avg requires arguments");
        }

        for (BlockNode argument : arguments) {
            ValueType type = argument.getType(context);

            if (
                    type != ValueType.NUMBER &&
                    type != ValueType.NUMBER_LIST
            ) {
                throw new IllegalStateException(
                        "avg only supports numbers or lists of numbers"
                );
            }
        }

        return ValueType.NUMBER;
    }
}
