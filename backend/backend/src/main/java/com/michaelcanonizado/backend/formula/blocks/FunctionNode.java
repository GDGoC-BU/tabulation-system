package com.michaelcanonizado.backend.formula.blocks;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.FormulaEvaluationException;
import com.michaelcanonizado.backend.exceptions.customs.FormulaTypeException;
import com.michaelcanonizado.backend.formula.contexts.EvaluationContext;
import com.michaelcanonizado.backend.formula.contexts.TypeContext;
import com.michaelcanonizado.backend.formula.functions.FormulaFunction;
import com.michaelcanonizado.backend.formula.values.Value;
import com.michaelcanonizado.backend.formula.values.ValueType;
import lombok.Getter;

import java.util.List;

/* To use a function, use this Node.
*
* Usage:
* BlockNode average =
*   new FunctionNode(
*     "average",
*     List.of(
*         new NumberLiteralNode(new BigDecimal("1")),
*         new NumberLiteralNode(new BigDecimal("2")),
*         new NumberLiteralNode(new BigDecimal("3"))
*     )
*   )
*
* Function name passed must be the name of the function that is already
* registered. And arguments are the arguements to be passed to the function.
*
* Think of this as a dropdown block where its options are the different
* function available. */
@Getter
public final class FunctionNode implements BlockNode {
    private final String functionName;
    private final List<BlockNode> arguments;

    @JsonCreator
    public FunctionNode(
            @JsonProperty("functionName") String functionName,
            @JsonProperty("arguments") List<BlockNode> arguments
    ) {
        this.functionName = functionName;
        this.arguments = arguments;
    }

    @Override
    public Value evaluate(EvaluationContext context) {
        /* Get the function definition from context.functionRegistry */
        FormulaFunction function = context.getFunctionRegistry().get(functionName);

        if (function == null) {
            throw new FormulaEvaluationException(
                    "Formula evaluation error! No registered function for FunctionNode: " + functionName,
                    ErrorCode.FORMULA_EVALUATION_ERROR
            );
        }

        /* Pass the arguments to the function and evaluate */
        return function.evaluate(arguments, context);
    }

    @Override
    public ValueType getType(TypeContext context) {
        FormulaFunction function = context
                .getFunctionRegistry()
                .get(functionName);

        if (function == null) {
            throw new FormulaTypeException(
                    "Invalid formula! No registered function for FunctionNode: " + functionName,
                    ErrorCode.FORMULA_TYPE_ERROR
            );
        }

        return function.getReturnType(arguments, context);
    }
}
