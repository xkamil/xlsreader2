package org.model;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public enum MatchType {
    EQUALS((a, b) -> a.trim().equalsIgnoreCase(b.trim())),
    STARTS_WITH((a, b) -> b.toLowerCase().startsWith(a.toLowerCase())),
    ENDS_WITH((a, b) -> b.toLowerCase().endsWith(a.toLowerCase())),
    CONTAINS((a, b) -> b.toLowerCase().contains(a.toLowerCase()));

    MatchType(BiFunction<String, String, Boolean> matcher) {
        this.matcher = matcher;
    }

    public boolean match(String input, String text) {
        return matcher.apply(input, text);
    }

    private BiFunction<String, String, Boolean> matcher;

    public static List<String> getValues() {
        return Arrays.stream(MatchType.values())
                .map(Enum::toString)
                .collect(Collectors.toList());
    }
}
