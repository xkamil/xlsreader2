package org.controller.view.component;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import java.io.IOException;

public class CancelButton extends Button {
    private final static double INITIAL_OPACITY = 0.8;
    private final static double HOVER_OPACITY = 0.5;


    public CancelButton() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/view/component/cancel_button.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        initView();
    }

    private void initView() {
        this.setOpacity(INITIAL_OPACITY);
        this.setOnMouseEntered(e -> setOpacity(HOVER_OPACITY));
        this.setOnMouseExited(e -> setOpacity(INITIAL_OPACITY));
    }
}
