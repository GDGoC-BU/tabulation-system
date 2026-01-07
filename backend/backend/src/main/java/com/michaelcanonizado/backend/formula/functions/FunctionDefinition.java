package com.michaelcanonizado.backend.formula.functions;

import com.michaelcanonizado.GDGoCTabulationSandbox.formula.blocks.BlockNode;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.contexts.EvaluationContext;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.contexts.TypeContext;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.Value;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.ValueType;

import java.util.List;

/* Models the function definition for higher level blocks */
public interface FunctionDefinition {
    Value evaluate(List<BlockNode> arguments, EvaluationContext context);
    ValueType getReturnType(List<BlockNode> arguments, TypeContext context);
}
