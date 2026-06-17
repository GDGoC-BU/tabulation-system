package com.gdgocbu.tabulation.backend.formula.functions;

import com.gdgocbu.tabulation.backend.formula.blocks.BlockNode;
import com.gdgocbu.tabulation.backend.formula.contexts.EvaluationContext;
import com.gdgocbu.tabulation.backend.formula.contexts.TypeContext;
import com.gdgocbu.tabulation.backend.formula.values.Value;
import com.gdgocbu.tabulation.backend.formula.values.ValueType;

import java.util.List;

/* Models the function definition for higher level blocks */
public interface FormulaFunction {
    String name();
    Value evaluate(List<BlockNode> arguments, EvaluationContext context);
    ValueType getReturnType(List<BlockNode> arguments, TypeContext context);
}
