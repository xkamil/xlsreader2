package org.model;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import java.util.ArrayList;
import java.util.List;

public class SuperCell {
    private final Cell cell;
    private final String sheetName;
    private final String workbookName;

    public SuperCell(Cell cell, String workbookName) {
        this.cell = cell;
        this.workbookName = workbookName.trim();
        this.sheetName = cell.getSheet().getSheetName().trim();
    }

    public List<SuperCell> getRow() {
        List<SuperCell> row = new ArrayList<>();

        for (Cell cell : cell.getRow()) {
            SuperCell sCell = new SuperCell(cell, getWorkbookName());
            row.add(sCell);
        }

        return row;
    }

    public String getWorkbookName() {
        return workbookName;
    }

    public String getSheetName() {
        return sheetName;
    }

    public String getHeader() {
        Cell headerCell = cell.getSheet().getRow(0).getCell(cell.getColumnIndex());

        if (headerCell == null) {
            return "";
        } else {
            return getCellStringValue(headerCell);
        }
    }

    public String getValue() {
        return getCellStringValue(cell);
    }

    private static String getCellStringValue(Cell cell) {
        CellType type = cell.getCellTypeEnum() == CellType.FORMULA ?
                cell.getCachedFormulaResultTypeEnum() : cell.getCellTypeEnum();

        switch (type) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue()).trim();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue()).trim();
            case BLANK:
            case ERROR:
            case _NONE:
            default:
                return "";
        }
    }

    @Override
    public String toString() {
        return "Cell{" +
                "VAL=" + getValue() +
                ", SHEET='" + sheetName + '\'' +
                ", WORKBOOK='" + workbookName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SuperCell superCell = (SuperCell) o;

        if (cell != null ? !cell.equals(superCell.cell) : superCell.cell != null) return false;
        if (sheetName != null ? !sheetName.equals(superCell.sheetName) : superCell.sheetName != null) return false;
        return workbookName != null ? workbookName.equals(superCell.workbookName) : superCell.workbookName == null;
    }

    @Override
    public int hashCode() {
        int result = cell != null ? cell.hashCode() : 0;
        result = 31 * result + (sheetName != null ? sheetName.hashCode() : 0);
        result = 31 * result + (workbookName != null ? workbookName.hashCode() : 0);
        return result;
    }
}

