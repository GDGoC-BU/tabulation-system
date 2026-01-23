package com.michaelcanonizado.backend.formula.blocks;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.michaelcanonizado.backend.formula.contexts.EvaluationContext;
import com.michaelcanonizado.backend.formula.contexts.TypeContext;
import com.michaelcanonizado.backend.formula.values.Value;
import com.michaelcanonizado.backend.formula.values.ValueType;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "nodeType"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = NumberLiteralNode.class, name = "NUMBER_LITERAL_NODE"),
        @JsonSubTypes.Type(value = BinaryOperationNode.class, name = "BINARY_OPERATION_NODE"),
        @JsonSubTypes.Type(value = CriterionNode.class, name = "CRITERION_NODE"),
        @JsonSubTypes.Type(value = FunctionNode.class, name = "FUNCTION_NODE"),
        @JsonSubTypes.Type(value = ListNode.class, name = "LIST_NODE")
})
/* Models a Block. Each block must implement its own BlockNode,
* defining their inputs, operations, and outputs. If the block acts
* like a function, use FunctionDefinition */
public sealed interface BlockNode permits
        NumberLiteralNode,
        CriterionNode,
        BinaryOperationNode,
        FunctionNode,
        ListNode
{
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
