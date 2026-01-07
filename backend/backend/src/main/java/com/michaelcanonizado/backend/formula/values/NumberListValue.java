package com.michaelcanonizado.backend.formula.values;

import java.math.BigDecimal;
import java.util.List;

public record NumberListValue(List<BigDecimal> values) implements Value {
}
