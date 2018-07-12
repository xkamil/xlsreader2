package org.controller;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import org.model.DataModel;

public class MainViewController {

    @FXML
    private ProgressFragmentController progressController;

    @Inject
    private DataModel dataModel;

    @FXML
    SearchFragmentController searchFragmentController;

    @FXML
    public void initialize() {
        dataModel.setOnLoadWorkbooksProgressListener(progressController);
    }

}
