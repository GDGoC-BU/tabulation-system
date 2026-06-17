package com.gdgocbu.tabulation.backend.formula.functions;

import com.gdgocbu.tabulation.backend.exceptions.common.ErrorCode;
import com.gdgocbu.tabulation.backend.exceptions.customs.FormulaEvaluationException;
import com.gdgocbu.tabulation.backend.exceptions.customs.FormulaTypeException;
import com.gdgocbu.tabulation.backend.formula.blocks.BlockNode;
import com.gdgocbu.tabulation.backend.formula.contexts.EvaluationContext;
import com.gdgocbu.tabulation.backend.formula.contexts.TypeContext;
import com.gdgocbu.tabulation.backend.formula.values.NumberListValue;
import com.gdgocbu.tabulation.backend.formula.values.NumberValue;
import com.gdgocbu.tabulation.backend.formula.values.Value;
import com.gdgocbu.tabulation.backend.formula.values.ValueType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public final class AverageFunction implements FormulaFunction {
    @Override
    public String name() {
        return "average";
    }

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
                throw new FormulaEvaluationException(
                        "Formula evaluation error! Encountered invalid argument types for \""+name()+"\" function: " +
                        "function only accepts [NumberValue,NumberListValue]. ",
                        ErrorCode.FORMULA_EVALUATION_ERROR
                );
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
            throw new FormulaTypeException(
                    "Invalid formula! \"" + name() + "\" function received empty arguments.",
                    ErrorCode.FORMULA_TYPE_ERROR
            );
        }

        for (BlockNode argument : arguments) {
            ValueType type = argument.getType(context);

            if (
                    type != ValueType.NUMBER &&
                    type != ValueType.NUMBER_LIST
            ) {
                throw new FormulaTypeException(
                        "Invalid formula! \"" + name() + "\" function only accepts: [ Number, NumberList ].",
                        ErrorCode.FORMULA_TYPE_ERROR
                );
            }
        }

        return ValueType.NUMBER;
    }
}
