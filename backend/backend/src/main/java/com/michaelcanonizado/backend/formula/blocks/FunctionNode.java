package com.michaelcanonizado.backend.formula.blocks;

import com.michaelcanonizado.backend.formula.contexts.EvaluationContext;
import com.michaelcanonizado.backend.formula.contexts.TypeContext;
import com.michaelcanonizado.backend.formula.functions.FormulaFunction;
import com.michaelcanonizado.backend.formula.values.Value;
import com.michaelcanonizado.backend.formula.values.ValueType;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
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
public class FunctionNode implements BlockNode {
    private final String functionName;
    private final List<BlockNode> arguments;

    @Override
    public Value evaluate(EvaluationContext context) {
        /* Get the function definition from context.functionRegistry */
        FormulaFunction function = context.getFunctionRegistry().get(functionName);

        if (function == null) {
            /* Custom unknown function error */
        }
        assert function != null;

        /* Pass the arguments to the function and evaluate */
        return function.evaluate(arguments, context);
    }

    @Override
    public ValueType getType(TypeContext context) {
        return context
                .getFunctionRegistry()
                .get(functionName)
                .getReturnType(arguments, context);
    }
}
