package com.michaelcanonizado.backend.formula.values;

import java.math.BigDecimal;

public record NumberValue(BigDecimal value) implements Value {
}
