package org.controller.view.section.fragment;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.model.DataModel;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FilterFragmentController {

    @FXML
    public TextField tvFilterBy;
    @FXML
    public VBox itemsList;
    @FXML
    public Label label;
    @FXML
    public CheckBox cbSelectAll;

    @Inject
    private DataModel dataModel;

    private List<String> selectedValues;


    private Consumer<List<String>> onSelectedValuesUpdated;

    @FXML
    public void initialize() {
        tvFilterBy.textProperty().addListener((observable, oldValue, newValue) -> filterDisplayedValues(newValue));
        cbSelectAll.setOnAction(e -> toggleSelectAllValues());
    }

    public void setOnSelectedValuesUpdated(Consumer<List<String>> onSelectedValuesUpdated) {
        this.onSelectedValuesUpdated = onSelectedValuesUpdated;
    }

    public void updateValues(Set<String> values) {
        itemsList.getChildren().clear();

        values.forEach(val -> {
            CheckBox checkBox = new CheckBox(val);
            checkBox.setOnAction(e -> updateSelectedValues());
            itemsList.getChildren().add(checkBox);
        });

        cbSelectAll.fire();
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    public void clearFilter() {
        System.out.println("Clear");
        tvFilterBy.textProperty().setValue("");
    }

    private void toggleSelectAllValues() {
        getCheckBoxes().stream()
                .filter(CheckBox::isVisible)
                .forEach(c -> {
                    if (cbSelectAll.isSelected() != c.isSelected()) {
                        c.fire();
                    }
                });
    }

    private List<CheckBox> getCheckBoxes() {
        return itemsList.getChildren().stream()
                .map(c -> (CheckBox) c)
                .collect(Collectors.toList());
    }

    private void filterDisplayedValues(String text) {
        getCheckBoxes().forEach(c -> {
            c.setVisible(false);
            c.setManaged(false);
        });
        getCheckBoxes().stream()
                .filter(c -> c.getText().toLowerCase().contains(text.toLowerCase()))
                .forEach(c -> {
                    c.setVisible(true);
                    c.setManaged(true);
                });
    }

    private void updateSelectedValues() {
        selectedValues = itemsList.getChildren().stream()
                .map(c -> (CheckBox) c)
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .collect(Collectors.toList());

        if (onSelectedValuesUpdated != null) {
            onSelectedValuesUpdated.accept(selectedValues);
        }
    }
}
