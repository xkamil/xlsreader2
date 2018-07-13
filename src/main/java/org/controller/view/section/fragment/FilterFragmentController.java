package org.controller.view.section.fragment;

import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import org.model.DataModel;
import org.model.FilterFactory;
import org.model.MatchType;
import org.model.SuperCell;
import org.model.filter.Filter;
import org.model.filter.FilterBy;
import org.model.filter.FilterSource;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FilterFragmentController implements FilterSource {

    @FXML private TextField tvFilterBy;
    @FXML private VBox itemsList;
    @FXML private Label label;
    @FXML private CheckBox cbSelectAll;
    @Inject private DataModel dataModel;
    @Inject private Filter filer;
    private FilterBy filterBy;

    @FXML
    public void initialize() {
        tvFilterBy.textProperty().addListener((observable, oldValue, newValue) -> filterDisplayedValues(newValue));
        cbSelectAll.setOnAction(e -> toggleSelectAllValues());
        filer.registerFilterSource(this);
    }

    public void updateItemsList(Set<String> values) {
        itemsList.getChildren().clear();

        values.forEach(val -> {
            CheckBox checkBox = new CheckBox(val);
            checkBox.setSelected(true);
            itemsList.getChildren().add(checkBox);
        });

        cbSelectAll.setSelected(true);
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    public void clearFilter() {
        tvFilterBy.textProperty().setValue("");
    }

    public void setFilterBy(FilterBy filterBy) {
        this.filterBy = filterBy;
    }

    private void toggleSelectAllValues() {
        getCheckBoxes().stream()
                .filter(CheckBox::isVisible)
                .forEach(c -> c.setSelected(cbSelectAll.isSelected()));
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

    @Override
    public Predicate<SuperCell> getFilter() {
        String[] selectedValues = this.getCheckBoxes().stream()
                .filter(CheckBox::isSelected)
                .map(CheckBox::getText)
                .toArray(String[]::new);

        return FilterFactory.build(filterBy, MatchType.EQUALS, selectedValues);
    }
}
