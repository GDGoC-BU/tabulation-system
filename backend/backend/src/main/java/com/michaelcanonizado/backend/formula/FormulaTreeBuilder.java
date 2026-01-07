package com.michaelcanonizado.backend.formula;

import com.fasterxml.jackson.databind.JsonNode;
import com.michaelcanonizado.backend.formula.blocks.BlockNode;
import com.michaelcanonizado.backend.formula.blocks.NumberLiteralNode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FormulaTreeBuilder {
    public BlockNode build(JsonNode rootNode) {
        return new NumberLiteralNode(new BigDecimal("0"));
    }
}
