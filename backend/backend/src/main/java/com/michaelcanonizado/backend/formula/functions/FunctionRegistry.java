package com.michaelcanonizado.backend.formula.functions;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public final class FunctionRegistry {
    private Map<String, FormulaFunction> functions = new HashMap<>();

    @Autowired
    public FunctionRegistry(List<FormulaFunction> functions) {
        /* Note: Spring Boot handles the injection of all formula functions.
        * Since the FunctionRegistry(this class) has the @Component annotation,
        * Spring Boot uses this constructor to initialize this class. It sees
        * that the constructor needs a List<FormulaFunction> and goes through
        * all classes that implement that interface, and pass it to the constructor.
        * The classes that implement the FormulaFunction interface also need to have
        * @Component annotation for it to be discovered.
        *
        * To use the FunctionRegistry and the registered functions, just @Autowire
        * it in the class that will use it. */

        this.functions = functions.stream()
                .collect(Collectors.toUnmodifiableMap(
                        FormulaFunction::name,
                        Function.identity()
                ));
    }

    public FormulaFunction get(String name) {
        return functions.get(name);
    }
}
