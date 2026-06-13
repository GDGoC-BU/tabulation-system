package com.gdgocbu.tabulation.backend.formula.contexts;

import com.gdgocbu.tabulation.backend.formula.functions.FunctionRegistry;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Map;
import java.util.UUID;

@AllArgsConstructor
@Getter
/* Stores the context needed to evaluate the formula.
* Evaluating each BlockNode might need information such as:
* 1) What's the scale?
* 2) What value should this criterion be?
*
* This handles that. The context is passed down the tree so
* each BlockNode has access to the "formula context" */
public final class EvaluationContext {
    private final MathContext mathContext;
    private final FunctionRegistry functionRegistry;
    private final Map<UUID, BigDecimal> criteriaValues;
}
