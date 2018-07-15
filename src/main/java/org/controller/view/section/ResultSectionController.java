package org.controller.view.section;

import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import org.model.DataModel;
import org.model.SuperCell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ResultSectionController {
    @FXML private Label statusBar;
    @FXML private TableView resultTable;
    @Inject private DataModel dataModel;
    private ObservableList<Map<String,String>> tableDataRows = FXCollections.observableArrayList();

    public void displayStatus(String status) {
        statusBar.setText(status);
    }

    public void displayCells(List<SuperCell> cellList) {
        createColumns();

        resultTable.setItems(tableDataRows);
        tableDataRows.clear();

        System.out.println(mapCellListToRowList(cellList));

        tableDataRows.addAll(mapCellListToRowList(cellList));
    }

    private List<Map<String,String>> mapCellListToRowList(List<SuperCell> cellList){
        List<Map<String, String>> rows = new ArrayList<>();

        cellList.stream().map(SuperCell::getRow).forEach(row->{
            rows.add(row.stream().collect(Collectors.toMap(SuperCell::getHeader, SuperCell::getValue)));
        });

        return rows;
    }

    private void createColumns() {
        //TODO fetch list of columns from filter
        resultTable.getColumns().clear();

        dataModel.getColumnsNames().stream()
                .map(columnName->{
                    TableColumn tc = new TableColumn(columnName);
                    tc.setCellValueFactory(new MapValueFactory<String>(columnName));
                    return tc;
                })
                .forEach(col -> resultTable.getColumns().add(col));
    }
}
