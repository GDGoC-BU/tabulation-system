package com.michaelcanonizado.backend.formula;

import com.fasterxml.jackson.databind.JsonNode;
import com.michaelcanonizado.backend.formula.blocks.BinaryOperationNode;
import com.michaelcanonizado.backend.formula.blocks.BinaryOperator;
import com.michaelcanonizado.backend.formula.blocks.BlockNode;
import com.michaelcanonizado.backend.formula.blocks.NumberLiteralNode;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FormulaTreeBuilder {
    private BlockNode buildBlock(JsonNode block) {
        /* Recursively convert each blockly block to a BlockNode */

        /* number_literal */
        if (block.get("type").asText().equals("number_literal")) {
            System.out.println("-> Creating number_literal");
            return new NumberLiteralNode(
                    new BigDecimal(
                            block.get("fields").get("VALUE").asText()
                    )
            );
        }

        /* criterion_dropdown */
        if (block.get("type").asText().equals("criterion_dropdown")) {
            System.out.println("-> creating criterion_dropdown");
            String criterionId = block.get("fields").get("CRITERION").asText();
            return new NumberLiteralNode(new BigDecimal("0"));
        }

        /* binary_operation */
        if (block.get("type").asText().equals("binary_operation")) {
            System.out.println("-> creating binary_operation");

            /* Recursively convert the left and right blockly block */
            JsonNode leftBlock = block
                    .get("inputs")
                    .get("LEFT_VALUE")
                    .get("block");
            JsonNode rightBlock = block
                    .get("inputs")
                    .get("RIGHT_VALUE")
                    .get("block");

            /* Convert the operation */
            String operatorString = block
                    .get("fields")
                    .get("OPERATOR")
                    .asText();
            BinaryOperator operator = switch (operatorString) {
                case "ADD" -> BinaryOperator.ADD;
                case "SUBTRACT" -> BinaryOperator.SUBTRACT;
                case "MULTIPLY" -> BinaryOperator.MULTIPLY;
                case "DIVIDE" -> BinaryOperator.DIVIDE;
                default -> throw new IllegalArgumentException("Unknown operator: " + operatorString);
            };

            return new BinaryOperationNode(
                    buildBlock(leftBlock),
                    operator,
                    buildBlock(rightBlock)
            );
        }

        throw new RuntimeException("Unknown block found!");
    }

    public BlockNode build(JsonNode serializedBlocklyWorkspace) {
        JsonNode blockyTopBlocks = serializedBlocklyWorkspace
                .get("blocks")
                .get("blocks");

        JsonNode blocklyFormulaRoot = null;
        for (JsonNode block : blockyTopBlocks) {
            if (block.get("type").asText().equals("formula_root")) {
                blocklyFormulaRoot = block;
            }
        }
        JsonNode block = blocklyFormulaRoot.get("inputs").get("FORMULA_RESULT").get("block");

        System.out.println("-> Begin");
        BlockNode root = buildBlock(block);
        System.out.println("-> End");
        return root;
    }
}
