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

    public Cell getCell() {
        return cell;
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

    public String getSheetName(){
        return sheetName;
    }

    public String getHeader() {
        Cell headerCell = cell.getSheet().getRow(0).getCell(cell.getColumnIndex());
        return new SuperCell(headerCell, "").getValue();
    }

    public String getValue() {
        String value;

        CellType type = cell.getCellTypeEnum() == CellType.FORMULA ?
                cell.getCachedFormulaResultTypeEnum() : cell.getCellTypeEnum();

        switch (type) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                value = String.valueOf((int) cell.getNumericCellValue());
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:
                value = "";
                break;
            case ERROR:
                value = "";
                break;
            case _NONE:
                value = "";
                break;
            default:
                value = "";
        }

        return value.trim();
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

