package org.model;

import javafx.application.Platform;
import javafx.concurrent.Task;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.util.WorkbookReader;

import java.io.File;
import java.io.IOException;
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
    private ProgressListener onLoadWorkbooksProgressListener;


    public DataModel() {
        filters = new HashMap<>();
        cells = new ArrayList<>();
        workbooks = new HashMap<>();
        onCellListChangeListeners = new HashSet<>();
    }

    public void setWorkbooks(File dir) {
        executeAsync(() -> {
            try {
                this.onLoadWorkbooksProgressListener.setLabel("Loading workbooks");
                this.onLoadWorkbooksProgressListener.show();
                this.workbooks = WorkbookReader.getAllWorkbooks(dir);
                this.extractCellsWithHeaders();
                Platform.runLater(this::notifyOnCellListChangeListeners);
                LOGGER.info(String.format("Loaded %d workbooks: %s", workbooks.size(), workbooks.keySet()));
            } catch (IOException e) {
                LOGGER.error(e);
                return;
            }finally {
                onLoadWorkbooksProgressListener.onProgressEnded();
            }
        });
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

    public void setOnLoadWorkbooksProgressListener(ProgressListener onLoadWorkbooksProgressListener) {
        this.onLoadWorkbooksProgressListener = onLoadWorkbooksProgressListener;
    }

    public void removeOnLoadWorkbooksProgressListener() {
        this.onLoadWorkbooksProgressListener = null;
    }

    private void notifyOnCellListChangeListeners() {
        onCellListChangeListeners.forEach(e -> e.accept(cells));

    }


    private void extractCellsWithHeaders() {
        List<SuperCell> cells = new ArrayList<>();
        AtomicInteger consumedSheets = new AtomicInteger(0);

        int maxValue = workbooks.values().stream()
                .mapToInt(Workbook::getNumberOfSheets)
                .reduce(Integer::sum).orElse(0);

        onLoadWorkbooksProgressListener.onProgressStarted(maxValue);

        for (Map.Entry<String, Workbook> workbookEntry : workbooks.entrySet()) {
            for (Sheet sheet : workbookEntry.getValue()) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if (row.getRowNum() != 0) {
                            cells.add(new SuperCell(cell, workbookEntry.getKey()));
                        }
                    }
                }
                onLoadWorkbooksProgressListener.onProgress(consumedSheets.incrementAndGet());
            }
        }
        this.cells = cells;
    }

    private static void executeAsync(Runnable run) {
        new Thread(run).start();
    }
}
