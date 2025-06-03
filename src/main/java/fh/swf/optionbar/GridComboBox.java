package fh.swf.optionbar;

import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import static fh.swf.Main.mainPane;

public class GridComboBox extends ComboBox<Integer> {
    public GridComboBox() {
        getItems().addAll(4, 8, 12, 16, 24, 32, 48, 64);
        setValue(1);

        setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) setText(null);
                else setText(item == 4 ? "1" : "1/" + item/4);
            }
        });
        setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) setText(null);
                else setText(item == 4 ? "1" : "1/" + item/4);
            }
        });

        valueProperty().addListener((_,_,newValue) -> mainPane.getPianoPane().getPianoGrid().getGridProperty().set(newValue));
    }
}
