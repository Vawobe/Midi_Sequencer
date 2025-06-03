package fh.swf.optionbar;

import fh.swf.render.GridRenderer;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

public class SignatureComboBox extends ComboBox<Integer> {
    public SignatureComboBox() {
        getItems().addAll(2, 3, 4, 5);
        setValue(4);

        setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) setText(null);
                else setText(item + "/4");
            }
        });
        setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item + "/4");
            }
        });

        valueProperty().addListener((_,_,newValue) -> GridRenderer.getInstance().getSignatureProperty().set(newValue));
    }


}
