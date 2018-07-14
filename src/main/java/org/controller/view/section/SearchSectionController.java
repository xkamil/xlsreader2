package org.controller.view.section;

import com.google.common.annotations.VisibleForTesting;
import com.google.inject.Inject;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import org.apache.log4j.Logger;
import org.model.DataModel;
import org.model.FilterFactory;
import org.model.MatchType;
import org.model.SuperCell;
import org.model.filter.Filter;
import org.model.filter.FilterBy;
import org.model.filter.FilterSource;

import java.io.File;
import java.util.List;
import java.util.function.Predicate;

public class SearchSectionController implements FilterSource {
    private static final Logger LOGGER = Logger.getLogger(SearchSectionController.class);

    @Inject private Filter filter;
    @Inject private DataModel dataModel;
    @FXML private ComboBox<String> cbMatchType;
    @FXML private TextField tvCellValue;
    @FXML private TextField tvSeparator;

    @FXML
    public void initialize() {
        cbMatchType.setItems(FXCollections.observableArrayList(MatchType.getValues()));
        cbMatchType.setValue(MatchType.getValues().get(0));
        filter.registerFilterSource(this);
    }

    @FXML
    public void onLoadWorkbooks() {
        File dir = chooseDirectory();
        dataModel.loadWorkbooks(dir);
    }

    @FXML
    public void onSearch() {
        List<SuperCell> filteredCells = filter.filter(dataModel.getCells()); //TODO

        LOGGER.info(String.format("Found %d cells with content %s : %s",
                filteredCells.size(),
                cbMatchType.getValue().toLowerCase(),
                tvCellValue.getText()));
    }

    @FXML
    public void clearFilter() {
        tvCellValue.textProperty().setValue("");
    }

    @Override
    public Predicate<SuperCell> getFilter() {
        MatchType matchType = MatchType.valueOf(cbMatchType.getValue());
        String separator = tvSeparator.getText();
        String[] values = separator.isEmpty() ? new String[]{tvCellValue.getText()} : tvCellValue.getText().split(separator);

        return FilterFactory.build(FilterBy.VALUE, matchType, values);
    }

    private File chooseDirectory() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Open directory with xlsx files");
        chooser.setInitialDirectory(new File("."));
        return chooser.showDialog(null);
    }
}
