package com.gdgocbu.tabulation.backend.utilities;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class CacheKeyBuilder {
    public String build(Object... parts) {
        String keySuffix = Arrays.stream(parts)
                .map(String::valueOf)
                .collect(Collectors.joining(":"));
        return String.format("%s", keySuffix);
    }
}
