package com.gdgocbu.tabulation.backend.models;

import com.fasterxml.jackson.databind.JsonNode;
import com.gdgocbu.tabulation.backend.converters.BlockNodeJsonConverter;
import com.gdgocbu.tabulation.backend.formula.blocks.BlockNode;
import io.hypersistence.utils.hibernate.type.json.JsonBinaryType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Formula {
    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String text;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            nullable = false,
            columnDefinition = "jsonb"
    )
    private JsonNode workspace;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(
            nullable = false,
            columnDefinition = "jsonb"
    )
    @Convert(converter = BlockNodeJsonConverter.class)
    private BlockNode tree;
}
