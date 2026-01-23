package com.michaelcanonizado.backend.formula;

import com.michaelcanonizado.backend.formula.blocks.BlockNode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@Getter
public class FormulaTree {
    private BlockNode rootNode;
    private Set<UUID> criterionIdsInFormula;
}
