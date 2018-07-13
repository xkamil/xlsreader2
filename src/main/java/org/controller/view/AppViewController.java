package org.controller.view;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import org.controller.view.section.SearchSectionController;
import org.model.DataModel;

public class AppViewController {

    @FXML
    private ProgressViewController progressController;

    @Inject
    private DataModel dataModel;

    @FXML
    SearchSectionController searchFragmentController;

    @FXML
    public void initialize() {
        dataModel.setOnLoadProgressListener(progressController);
    }

}
