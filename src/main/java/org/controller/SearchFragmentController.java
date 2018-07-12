package org.controller;

import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Workbook;
import org.model.DataModel;
import org.model.FilterFactory;
import org.model.MatchType;
import org.model.SuperCell;
import org.util.WorkbookReader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

public class SearchFragmentController {
    private static final Logger LOGGER = Logger.getLogger(SearchFragmentController.class);

    @Inject
    private DataModel dataModel;

    @FXML
    private ComboBox<String> cbMatchType;

    @FXML
    private TextField tvCellValue;

    @FXML
    public void initialize() {
        cbMatchType.setItems(FXCollections.observableArrayList(MatchType.getValues()));
        cbMatchType.setValue(MatchType.getValues().get(0));
    }

    @FXML
    public void onSelectMatchType(ActionEvent e) {
        System.out.println("Selected match type: " + cbMatchType.getValue()); //TODO
    }

    @FXML
    public void onLoadWorkbooks(){
        File dir = chooseDirectory();
        dataModel.setWorkbooks(dir);
    }

    @FXML
    public void onSearch() {
        MatchType matchType = MatchType.valueOf(cbMatchType.getValue());
        String text = tvCellValue.getText();
        dataModel.setFilter(FilterFactory.VALUE, FilterFactory.byValue(text, matchType));

        List<SuperCell> result = dataModel.getFilteredCells();

        LOGGER.info(String.format("Found %d cells with content %s : %s", result.size(), matchType.toString().toLowerCase(), text));
    }

    private File chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open directory with xlsx files");
        chooser.setInitialDirectory(new File("."));
        return chooser.showDialog(null);
    }

    @FXML
    public void clearFilter() {
        tvCellValue.textProperty().setValue("");
    }
}
