package org.model;

import javafx.application.Platform;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.util.WorkbookReader;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class DataModel {
    private static final Logger LOGGER = Logger.getLogger(DataModel.class);

    private Map<String, Workbook> workbooks = new HashMap<>();
    private List<SuperCell> cells = new ArrayList<>();
    private Set<Consumer<List<SuperCell>>> onCellListChangeListeners = new HashSet<>();
    private ProgressListener onLoadProgressListener;

    public void loadWorkbooks(File directory) {
        new Thread(() -> {
            try {
                this.onLoadProgressListener.show("Loading workbooks");
                this.workbooks = WorkbookReader.getAllWorkbooks(directory);
                this.extractCellsWithHeaders();
                Platform.runLater(this::notifyOnCellListChangeListeners);
                LOGGER.info(String.format("Loaded %d workbooks: %s", workbooks.size(), workbooks.keySet()));
            } catch (IOException e) {
                LOGGER.error(e);
            } finally {
                onLoadProgressListener.onProgressEnded();
            }
        }).start();
    }

    public Set<String> getWorkbooksNames() {
        return workbooks.keySet();
    }

    public Set<String> getSheetsNames() {
        return cells.parallelStream()
                .map(SuperCell::getSheetName)
                .collect(Collectors.toSet());
    }

    public Set<String> getColumnsNames() {
        return cells.parallelStream()
                .map(SuperCell::getHeader)
                .collect(Collectors.toSet());
    }

    public void addOnCellListChangeListener(Consumer<List<SuperCell>> listener) {
        onCellListChangeListeners.add(listener);
    }

    public void setOnLoadProgressListener(ProgressListener onLoadProgressListener) {
        this.onLoadProgressListener = onLoadProgressListener;
    }

    public List<SuperCell> getCells() {
        return cells;
    }

    private void notifyOnCellListChangeListeners() {
        onCellListChangeListeners.forEach(e -> e.accept(cells));
    }

    private void extractCellsWithHeaders() {
        AtomicInteger consumedSheets = new AtomicInteger(0);
        onLoadProgressListener.onProgressStarted(getSheetsCount());

        for (Map.Entry<String, Workbook> workbookEntry : workbooks.entrySet()) {
            for (Sheet sheet : workbookEntry.getValue()) {
                for (Row row : sheet) {
                    for (Cell cell : row) {
                        if (row.getRowNum() != 0) {
                            this.cells.add(new SuperCell(cell, workbookEntry.getKey()));
                        }
                    }
                }
                onLoadProgressListener.onProgress(consumedSheets.incrementAndGet());
            }
        }
    }

    private int getSheetsCount() {
        return workbooks.values().stream()
                .mapToInt(Workbook::getNumberOfSheets)
                .reduce(Integer::sum).orElse(0);
    }
}
