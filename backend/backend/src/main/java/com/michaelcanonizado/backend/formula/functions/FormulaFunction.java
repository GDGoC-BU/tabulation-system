package com.michaelcanonizado.backend.formula.functions;

import com.michaelcanonizado.backend.formula.blocks.BlockNode;
import com.michaelcanonizado.backend.formula.contexts.EvaluationContext;
import com.michaelcanonizado.backend.formula.contexts.TypeContext;
import com.michaelcanonizado.backend.formula.values.Value;
import com.michaelcanonizado.backend.formula.values.ValueType;

import java.util.List;

/* Models the function definition for higher level blocks */
public interface FormulaFunction {
    String name();
    Value evaluate(List<BlockNode> arguments, EvaluationContext context);
    ValueType getReturnType(List<BlockNode> arguments, TypeContext context);
}
