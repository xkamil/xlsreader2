package org.controller.view.section;

import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.FlowPane;
import org.model.DataModel;
import org.model.SuperCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultSectionController {
    private static final double FILTER_BUTTON_MAX_WIDTH = 200;
    private static final double TABLE_COLUMN_MAX_WIDTH = 300;

    @FXML public TitledPane filterDisplayedColumnsContainer;
    @FXML private FlowPane containerForBtnFilterColumn;
    @FXML private Label statusBar;
    @FXML private TableView resultTable;
    @Inject
    private DataModel dataModel;
    private List<BtnColumnFilter> columnFiltersButtonsList = new ArrayList<>();
    private List<TableColumn> tableColumns = new ArrayList<>();
    private ObservableList<Map<String, String>> tableDataRows = FXCollections.observableArrayList();


    public void displayStatus(String status) {
        statusBar.setText(status);
    }

    public void displayCells(List<SuperCell> cellList) {
        addFilterColumnsButtons();
        createColumns();

        resultTable.setItems(tableDataRows);
        tableDataRows.clear();
        tableDataRows.addAll(mapCellListToRowList(cellList));
    }

    @FXML
    public void selectAll() {
        columnFiltersButtonsList.stream()
                .filter(btn->!btn.isSelected())
                .forEach(ToggleButton::fire);
    }

    @FXML
    public void selectNone() {
        columnFiltersButtonsList.stream()
                .filter(ToggleButton::isSelected)
                .forEach(ToggleButton::fire);
    }

    private List<Map<String, String>> mapCellListToRowList(List<SuperCell> cellList) {
        List<Map<String, String>> rows = new ArrayList<>();

        cellList.stream().map(SuperCell::getRow).forEach(row -> {
            rows.add(row.stream().collect(Collectors.toMap(SuperCell::getHeader, SuperCell::getValue)));
        });

        return rows;
    }

    private void createColumns() {
        resultTable.getColumns().clear();
        tableColumns.clear();

        dataModel.getColumnsNames()
                .forEach(columnName -> {
                    TableColumn tc = new TableColumn(columnName);
                    tc.setMaxWidth(TABLE_COLUMN_MAX_WIDTH);
                    tableColumns.add(tc);
                    tc.setCellValueFactory(new MapValueFactory<String>(columnName));
                });

        resultTable.getColumns().addAll(tableColumns);
    }

    private void addFilterColumnsButtons() {
        containerForBtnFilterColumn.getChildren().clear();
        columnFiltersButtonsList.clear();
        dataModel.getColumnsNames().forEach(col -> {
            BtnColumnFilter btn = new BtnColumnFilter(col);
            btn.setMaxWidth(FILTER_BUTTON_MAX_WIDTH);
            columnFiltersButtonsList.add(btn);
        });
        containerForBtnFilterColumn.getChildren().addAll(columnFiltersButtonsList);
    }

    private class BtnColumnFilter extends ToggleButton {
        public BtnColumnFilter(String name) {
            super(name);
            init();
        }

        private void init() {
            this.setSelected(true);
            this.setOnAction(a -> tableColumns.stream()
                    .filter(tc -> tc.getText().equals(this.getText()))
                    .forEach(tc -> tc.setVisible(this.isSelected())));
        }
    }
}
