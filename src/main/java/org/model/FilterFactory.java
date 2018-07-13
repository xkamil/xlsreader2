package org.model;

import org.model.filter.FilterBy;

import java.util.Arrays;
import java.util.function.Predicate;

public final class FilterFactory {
    public static Predicate<SuperCell> build(FilterBy by, MatchType matchType, String... values) {
        return cell -> Arrays.stream(values).anyMatch(w -> matchType.match(w, getCellAttribute(cell, by)));
    }

    private static String getCellAttribute(SuperCell cell, FilterBy by) {

        switch (by) {
            case WORKBOOK:
                return cell.getWorkbookName();
            case SHEET:
                return cell.getSheetName();
            case COLUMN:
                return cell.getHeader();
            case VALUE:
                return cell.getValue();
            default:
                return "";
        }
    }
}
