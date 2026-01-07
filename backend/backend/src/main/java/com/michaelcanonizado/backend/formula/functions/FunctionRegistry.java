package com.michaelcanonizado.backend.formula.functions;

import java.util.HashMap;
import java.util.Map;

public class FunctionRegistry {
    private final Map<String, FunctionDefinition> functions = new HashMap<>();

    public void register(String name, FunctionDefinition evaluator) {
        functions.put(name, evaluator);
    }

    public FunctionDefinition get(String name) {
        return functions.get(name);
    }
}
