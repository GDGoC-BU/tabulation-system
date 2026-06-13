package com.gdgocbu.tabulation.backend.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdgocbu.tabulation.backend.formula.blocks.BlockNode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class BlockNodeJsonConverter implements AttributeConverter<BlockNode, JsonNode> {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public JsonNode convertToDatabaseColumn(BlockNode attribute) {
        return attribute == null ? null : MAPPER.valueToTree(attribute);
    }

    @Override
    public BlockNode convertToEntityAttribute(JsonNode dbData) {
        try {
            return dbData == null
                    ? null
                    : MAPPER.treeToValue(dbData, BlockNode.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize BlockNode", e);
        }
    }
}





