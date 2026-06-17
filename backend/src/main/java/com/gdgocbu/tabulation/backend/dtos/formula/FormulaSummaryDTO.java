package com.gdgocbu.tabulation.backend.dtos.formula;

import com.fasterxml.jackson.databind.JsonNode;

public record FormulaSummaryDTO(
        String text,
        JsonNode workspace
) {
}
