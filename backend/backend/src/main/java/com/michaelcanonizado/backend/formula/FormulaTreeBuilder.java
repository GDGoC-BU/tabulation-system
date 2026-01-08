package com.michaelcanonizado.backend.formula;

import com.fasterxml.jackson.databind.JsonNode;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.FormulaTreeException;
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
        String blockType = block.get("type").asText();

        /* number_literal */
        switch (blockType) {
            case "number_literal" -> {
                return new NumberLiteralNode(
                        new BigDecimal(
                                block.get("fields").get("VALUE").asText()
                        )
                );
            }


            /* criterion_dropdown */
            case "criterion_dropdown" -> {
                String criterionId = block.get("fields").get("CRITERION").asText();
                return new NumberLiteralNode(new BigDecimal("0"));
            }


            /* binary_operation */
            case "binary_operation" -> {
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
                String operatorString = block.get("fields").get("OPERATOR").asText();
                BinaryOperator operator = switch (operatorString) {
                    case "ADD" -> BinaryOperator.ADD;
                    case "SUBTRACT" -> BinaryOperator.SUBTRACT;
                    case "MULTIPLY" -> BinaryOperator.MULTIPLY;
                    case "DIVIDE" -> BinaryOperator.DIVIDE;
                    default -> {
                        throw new FormulaTreeException(
                                "Unknown binary_operation operator: " + operatorString,
                                ErrorCode.FORMULA_TREE_BUILDING_ERROR
                        );
                    }
                };

                return new BinaryOperationNode(
                        buildBlock(leftBlock),
                        operator,
                        buildBlock(rightBlock)
                );
            }
        }

        throw new FormulaTreeException(
                "Can't determine BlockNode for blocky block: " + blockType,
                ErrorCode.FORMULA_TREE_BUILDING_ERROR
        );
    }

    public BlockNode build(JsonNode serializedBlocklyWorkspace) {
        try {
            /* Directly read from the JsonNode serialized workspace to avoid writing verbose
            classes to deserialize it to java classes. */

            /* Get the "formula_root" block */
            JsonNode blockyTopBlocks = serializedBlocklyWorkspace.get("blocks").get("blocks");
            JsonNode blocklyFormulaRoot = null;
            for (JsonNode block : blockyTopBlocks) {
                if (block.get("type").asText().equals("formula_root")) {
                    blocklyFormulaRoot = block;
                }
            }

            if (blocklyFormulaRoot == null || blocklyFormulaRoot.isNull()) {
                throw new FormulaTreeException(
                        "Serialized workspace doesnt contain \"formula_root\" block!",
                        ErrorCode.FORMULA_TREE_BUILDING_ERROR
                );
            }

            /* Get the block connected to "formula_root", this will be the root of the formula tree */
            JsonNode formulaInput = blocklyFormulaRoot.get("inputs").get("FORMULA_RESULT").get("block");

            /* Recursively build the formula tree */
            return buildBlock(formulaInput);
        }
        /* Catches JsonNode and the custom class exceptions in the /formula package */
        catch (Exception e) {
            throw new FormulaTreeException(
                    "Error parsing serialized blockly workspace to formula tree: \nError: " + e.getMessage(),
                    ErrorCode.FORMULA_TREE_BUILDING_ERROR
            );
        }
    }
}
