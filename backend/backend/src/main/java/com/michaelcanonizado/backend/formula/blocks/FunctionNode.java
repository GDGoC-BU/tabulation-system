package com.michaelcanonizado.backend.formula.blocks;

import com.michaelcanonizado.GDGoCTabulationSandbox.formula.contexts.EvaluationContext;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.contexts.TypeContext;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.functions.FunctionDefinition;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.Value;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.ValueType;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class FunctionNode implements BlockNode {
    private final String functionName;
    private final List<BlockNode> arguments;

    @Override
    public Value evaluate(EvaluationContext context) {
        /* Get the function definition from the registry */
        FunctionDefinition evaluator = context.getFunctionRegistry().get(functionName);

        if (evaluator == null) {
            /* Custom unknown function error */
        }
        assert evaluator != null;

        /* Pass the arguments to the function and evaluate */
        return evaluator.evaluate(arguments, context);
    }

    @Override
    public ValueType getType(TypeContext context) {
        return context
                .getFunctionRegistry()
                .get(functionName)
                .getReturnType(arguments, context);
    }
}
