package org.model;

import org.model.filter.FilterBy;

import java.util.Arrays;
import java.util.function.Predicate;

public final class FilterFactory {
    public static final String WORKBOOK = "workbook";
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

    public static Predicate<SuperCell> build( FilterBy by, MatchType matchType, String... values) {
        return cell -> {
            String matchAgainst = null;

            switch (by) {
                case WORKBOOK:
                    matchAgainst = cell.getWorkbookName();
                    break;
                case SHEET:
                    matchAgainst = cell.getSheetName();
                    break;
                case COLUMN:
                    matchAgainst = cell.getHeader();
                    break;
                case VALUE:
                    matchAgainst = cell.getValue();
                    break;
            }

            return  Arrays.stream(values).anyMatch(w -> matchAgainst.equalsIgnoreCase(w);
        };
    }
}
