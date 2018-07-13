package org.controller.view;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import org.apache.log4j.Logger;
import org.model.ProgressListener;


public class ProgressViewController implements ProgressListener {

    private static final Logger LOGGER = Logger.getLogger(ProgressViewController.class);

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

    @Override
    public void onProgressStarted(double maxValue) {
        Platform.runLater(()-> this.maxValue = maxValue);
    }

    @Override
    public void onProgress(double currentValue) {
        Platform.runLater(()-> progressBar.setProgress(((100 / maxValue) * currentValue) * 0.01));
    }

    @Override
    public void onProgressEnded() {
        Platform.runLater(this::hide);
    }

    @Override
    public void show(String label) {
        Platform.runLater(()->{
            progressBar.setProgress(0.0);
            progressLabel.setText(label);
            progressPane.setVisible(true);
        });
    }

    private void hide() {
        progressPane.setVisible(false);
    }
}
