package com.michaelcanonizado.backend.formula.contexts;

import com.michaelcanonizado.backend.formula.functions.FunctionRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
/* Stores the context needed to get the type of BlockNodes.
 * A separate context is created to avoid future bugs.
 * The BlockNode.getType() and FormulaFunction.getReturnType()
 * methods should only be limited to what context they have access to.
 *
 * This prevents code like:
 *
 * public class NumberLiteralNode implements BlockNode {
 *      public Value evaluate(EvaluationContext context) {
 *           ...
 *      }
 *
 *      public ValueType getType(TypeContext context) {
 *           evaluate(context) // Not possible
 *           return ValueType.NUMBER;
 *      }
 * }
 * */
public final class TypeContext {
    private final FunctionRegistry functionRegistry;
}
