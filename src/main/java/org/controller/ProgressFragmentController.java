package org.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.model.ProgressListener;


public class ProgressFragmentController implements ProgressListener {

    private static final Logger LOGGER = Logger.getLogger(ProgressFragmentController.class);

    @FXML
    public Label progressLabel;
    @FXML
    public ProgressBar progressBar;
    @FXML
    private VBox progressPane;

    private double maxValue;

    @FXML
    public void initialize() {
        hide();
    }

    public void setLabel(String label) {
        Platform.runLater(()->progressLabel.setText(label));
    }

    @Override
    public void onProgressStarted(double maxValue) {
        Platform.runLater(()->{
            this.maxValue = maxValue;
            progressBar.setProgress(0.0);
            show();
        });
    }

    @Override
    public void onProgress(double currentValue) {
        Platform.runLater(()-> progressBar.setProgress(((100 / maxValue) * currentValue) * 0.01));
    }

    @Override
    public void onProgressEnded() {
        Platform.runLater(()->{
            progressBar.setProgress(1);
            hide();
        });
    }

    @Override
    public void show() {
        Platform.runLater(()->progressPane.setVisible(true));

    }

    private void hide() {
        progressPane.setVisible(false);
    }
}
