package org.controller.view.section;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import org.controller.view.section.fragment.FilterFragmentController;
import org.model.DataModel;
import org.model.filter.FilterBy;

public class FilterSectionController {
    @FXML private FilterFragmentController filterByWorkbooksController;
    @FXML private FilterFragmentController filterBySheetsController;
    @FXML private FilterFragmentController filterByColumnsController;
    @Inject private DataModel dataModel;

    @FXML
    public void initialize() {
        filterByWorkbooksController.setLabel("Workbooks");
        filterByWorkbooksController.setFilterBy(FilterBy.WORKBOOK);

        filterBySheetsController.setLabel("Sheets");
        filterBySheetsController.setFilterBy(FilterBy.SHEET);

        filterByColumnsController.setLabel("Columns");
        filterByColumnsController.setFilterBy(FilterBy.COLUMN);

        dataModel.addOnCellListChangeListener(cells -> {
            filterByWorkbooksController.updateItemsList(dataModel.getWorkbooksNames());
            filterBySheetsController.updateItemsList(dataModel.getSheetsNames());
            filterByColumnsController.updateItemsList(dataModel.getColumnsNames());
        });
    }
}
