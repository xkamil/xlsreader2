package org.model;

import java.util.Arrays;
import java.util.function.Predicate;

public final class FilterFactory {
    public static final String WORKBOOOK = "workbook";
    public static final String SHEET = "sheet";
    public static final String HEADER = "header";
    public static final String VALUE = "value";

    public static Predicate<SuperCell> byWorkbooks(String... workbooks) {
        return cell -> Arrays.stream(workbooks).anyMatch(w -> cell.getWorkbookName().equalsIgnoreCase(w));
    }

    public static Predicate<SuperCell> byHeaders(String... headers) {
        return cell -> Arrays.stream(headers).anyMatch(w -> cell.getHeader().equalsIgnoreCase(w));
    }

    public static Predicate<SuperCell> bySheets(String... sheets) {
        return cell -> Arrays.stream(sheets).anyMatch(w -> cell.getSheetName().equalsIgnoreCase(w));
    }

    public static Predicate<SuperCell> byValue(String value, MatchType matchType) {
        return cell -> matchType.match(value.toLowerCase(), cell.getValue().toLowerCase());
    }
}
