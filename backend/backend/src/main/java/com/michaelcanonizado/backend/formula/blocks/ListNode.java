package com.michaelcanonizado.backend.formula.blocks;

import com.michaelcanonizado.backend.formula.contexts.EvaluationContext;
import com.michaelcanonizado.backend.formula.contexts.TypeContext;
import com.michaelcanonizado.backend.formula.values.NumberListValue;
import com.michaelcanonizado.backend.formula.values.NumberValue;
import com.michaelcanonizado.backend.formula.values.Value;
import com.michaelcanonizado.backend.formula.values.ValueType;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class ListNode implements BlockNode {
    private final List<BlockNode> elements;

    @Override
    public Value evaluate(EvaluationContext context) {
        List<BigDecimal> values = new ArrayList<>();

        /* Since the list can contain any BlockNode, evaluate
        * each node to get the actual value of the element. */
        for (BlockNode element : elements) {
            Value value = element.evaluate(context);

            /* Check the type of each element */
            if (value instanceof NumberValue(BigDecimal value1)) {
                values.add(value1);
            } else {
                throw new IllegalStateException("List must only contain NUMBERS");
            }
        }

        return new NumberListValue(values);
    }

    @Override
    public ValueType getType(TypeContext context) {
        for (BlockNode element : elements) {
            ValueType elementType = element.getType(context);

            if (elementType != ValueType.NUMBER) {
                throw new IllegalStateException("List must only contain NUMBERS" );
            }
        }

        return ValueType.NUMBER_LIST;
    }
}
