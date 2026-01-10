package com.michaelcanonizado.backend.formula;

import com.fasterxml.jackson.databind.JsonNode;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.FormulaInvalidWorkspaceException;
import com.michaelcanonizado.backend.formula.blocks.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class FormulaTreeBuilder {
    private BlockNode parseNumberLiteral(JsonNode block) {
        return new NumberLiteralNode(
                new BigDecimal(
                        block.get("fields").get("VALUE").asText()
                )
        );
    }

    private BlockNode parseCriterionDropdown(JsonNode block, Set<UUID> criterionIdCollector) {
        String criterionId = block.get("fields").get("CRITERION").asText().trim();
        try {
            UUID criterionUUID = UUID.fromString(criterionId);
            criterionIdCollector.add(criterionUUID);
            return new CriterionNode(criterionUUID);
        } catch (Exception e) {
            throw new FormulaInvalidWorkspaceException(
                    "Error building Formula Tree. Cannot convert criterion_dropdown value: \"" + criterionId + "\" to a UUID",
                    ErrorCode.FORMULA_TREE_BUILDING_ERROR
            );
        }
    }

    private BlockNode parseBinaryOperation(JsonNode block, Set<UUID> criterionIdCollector) {
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
                throw new FormulaInvalidWorkspaceException(
                        "Unknown blockly binary_operation operator: " + operatorString,
                        ErrorCode.FORMULA_TREE_BUILDING_ERROR
                );
            }
        };

        return new BinaryOperationNode(
                buildBlock(leftBlock, criterionIdCollector),
                operator,
                buildBlock(rightBlock, criterionIdCollector)
        );
    }

    private BlockNode buildBlock(JsonNode block, Set<UUID> criterionIdCollector) {
        /* Recursively convert each blockly block to a BlockNode */
        String blockType = block.get("type").asText();

        return switch (blockType) {
            case "number_literal" -> parseNumberLiteral(block);
            case "criterion_dropdown" -> parseCriterionDropdown(block, criterionIdCollector);
            case "binary_operation" -> parseBinaryOperation(block, criterionIdCollector);
            default -> throw new FormulaInvalidWorkspaceException(
                    "FormulaTreeBuilder can't determine BlockNode for blocky block: " + blockType,
                    ErrorCode.FORMULA_TREE_BUILDING_ERROR
            );
        };

    }

    public FormulaTree build(JsonNode serializedBlocklyWorkspace) {
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
                throw new FormulaInvalidWorkspaceException(
                        "Serialized workspace doesnt contain \"formula_root\" block!",
                        ErrorCode.FORMULA_TREE_BUILDING_ERROR
                );
            }

            /* Get the block connected to "formula_root", this will be the root of the formula tree */
            JsonNode formulaInput = blocklyFormulaRoot.get("inputs").get("FORMULA_RESULT").get("block");

            Set<UUID> criterionIdCollector = new HashSet<>();

            /* Recursively build the formula tree */
            return new FormulaTree(
                    buildBlock(formulaInput, criterionIdCollector),
                    criterionIdCollector
            );
        }
        /* NOTE: Any exceptions with formula evaluation and type checking happens when you call
            formula.evaluate() or formula.getType() on the formula (Outside this class). */
        /* Catches JsonNode exceptions */
        catch (NullPointerException | IllegalArgumentException e) {
            throw new FormulaInvalidWorkspaceException(
                    "Error parsing serialized blockly workspace to formula tree: \nError: " + e.getMessage(),
                    ErrorCode.FORMULA_TREE_BUILDING_ERROR
            );
        }
    }
}
