package org.model;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;

import java.util.stream.Collectors;

public class DataModel {
    private static final Logger LOGGER = Logger.getLogger(DataModel.class);

    private Map<String, Workbook> workbooks;
    private List<SuperCell> cells;
    private Map<String, Predicate<SuperCell>> filters;

    private Set<Consumer<List<SuperCell>>> onCellListChangeListeners;
    private Consumer<Integer> onLoadWorkbooksProgressListener;


    public DataModel() {
        filters = new HashMap<>();
        cells = new ArrayList<>();
        workbooks = new HashMap<>();
        onCellListChangeListeners = new HashSet<>();
    }

    public void setWorkbooks(Map<String, Workbook> workbooks) {
        this.workbooks = workbooks;
        this.extractCellsWithHeaders();
        notifyOnCellListChangeListeners();
    }

    public List<SuperCell> getCells() {
        return this.cells;
    }

    public Set<String> getWorkbooks() {
        return workbooks.keySet();
    }

    public Set<String> getSheets() {
        return cells.stream()
                .map(SuperCell::getSheetName)
                .collect(Collectors.toSet());
    }

    public Set<String> getHeaders() {
        return cells.stream()
                .map(SuperCell::getHeader)
                .collect(Collectors.toSet());
    }

    public List<SuperCell> getFilteredCells() {
        return cells.stream()
                .filter(cell -> filters.values().stream().filter(f -> f.test(cell)).count() == filters.size())
                .collect(Collectors.toList());
    }

    public void setFilter(String id, Predicate<SuperCell> filter) {
        LOGGER.info(String.format("Setting %s filter", id));
        filters.put(id, filter);
    }

    public void addOnCellListChangeListener(Consumer<List<SuperCell>> listener) {
        onCellListChangeListeners.add(listener);
    }

    public void removeOnCellListChangeListener(Consumer<List<SuperCell>> listener) {
        onCellListChangeListeners.remove(listener);
    }

    public void setOnLoadWorkbooksProgressListener(Consumer<Integer> onLoadWorkbooksProgressListener) {
        this.onLoadWorkbooksProgressListener = onLoadWorkbooksProgressListener;
    }

    public void removeOnLoadWorkbooksProgressListener() {
        this.onLoadWorkbooksProgressListener = null;
    }

    private void notifyOnCellListChangeListeners() {
        onCellListChangeListeners.forEach(e -> e.accept(cells));
    }

    private void notifyProgressListener(final int consumedSheets){
        workbooks.values().stream()
                .mapToInt(Workbook::getNumberOfSheets)
                .reduce(Integer::sum)
                .ifPresent(sheets -> {
                    if (onLoadWorkbooksProgressListener != null) {
                        int progress = (int)((double)consumedSheets / sheets * 100);
                        onLoadWorkbooksProgressListener.accept(progress);
                    }
                });
    }

    private void extractCellsWithHeaders() {
        List<SuperCell> cells = new ArrayList<>();
        AtomicInteger consumedSheets = new AtomicInteger(0);

        for (Map.Entry<String, Workbook> workbookEntry : workbooks.entrySet()) {
            for (Sheet sheet : workbookEntry.getValue()) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if (row.getRowNum() != 0) {
                            cells.add(new SuperCell(cell, workbookEntry.getKey()));
                        }
                    }

                }
                notifyProgressListener(consumedSheets.incrementAndGet());
            }
        }

        this.cells = cells;
    }
}
