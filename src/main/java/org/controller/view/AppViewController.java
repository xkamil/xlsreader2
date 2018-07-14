package org.controller.view;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import org.controller.view.section.SearchSectionController;
import org.model.DataModel;

public class AppViewController {

    @FXML private ProgressViewController progressController;
    @FXML SearchSectionController searchFragmentController;
    @Inject private DataModel dataModel;

    @FXML
    public void initialize() {
        dataModel.setOnLoadProgressListener(progressController);
    }

}
