package org.controller;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import org.model.DataModel;
import org.model.FilterFactory;

public class FiltersFragmentController {


    @FXML
    public FilterFragmentController filterByWorkbooksController;
    @FXML
    public FilterFragmentController filterBySheetsController;
    @FXML
    public FilterFragmentController filterByColumnsController;

    @Inject
    private DataModel dataModel;

    @FXML
    public void initialize() {

        filterByWorkbooksController.setLabel("Workbooks");
        filterByWorkbooksController.setOnSelectedValuesUpdated(val -> dataModel
                .setFilter(FilterFactory.WORKBOOK, FilterFactory.byWorkbooks(val.toArray(new String[0]))));

        filterBySheetsController.setLabel("Sheets");
        filterBySheetsController.setOnSelectedValuesUpdated(val -> dataModel
                .setFilter(FilterFactory.SHEET, FilterFactory.bySheets(val.toArray(new String[0]))));

        filterByColumnsController.setLabel("Columns");
        filterByColumnsController.setOnSelectedValuesUpdated(val -> dataModel
                .setFilter(FilterFactory.HEADER, FilterFactory.byHeaders(val.toArray(new String[0]))));

        dataModel.addOnCellListChangeListener(cells -> {
            filterByWorkbooksController.updateValues(dataModel.getWorkbooks());
            filterBySheetsController.updateValues(dataModel.getSheets());
            filterByColumnsController.updateValues(dataModel.getHeaders());
        });
    }

}
