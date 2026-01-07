package com.michaelcanonizado.backend.formula.blocks;

import com.michaelcanonizado.GDGoCTabulationSandbox.formula.contexts.EvaluationContext;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.contexts.TypeContext;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.Value;
import com.michaelcanonizado.GDGoCTabulationSandbox.formula.values.ValueType;

/* Models a Block. Each block must implement its own BlockNode,
* defining their inputs, operations, and outputs. If the block acts
* like a function, use FunctionDefinition */
public interface BlockNode {
    /* Performs the operation of the block and returns the result.
    * Leaf nodes will immediately return their value. */
    Value evaluate(EvaluationContext context);
    /* Verifies that the block has invalid input types. Must go through
    * all inputs, and call input.getType() recursively.
    *
    * Return the output type of the Block.
    * Leaf nodes will immediately return their type. */
    ValueType getType(TypeContext context);
}
