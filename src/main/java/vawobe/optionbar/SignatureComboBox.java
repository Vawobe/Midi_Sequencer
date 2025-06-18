package vawobe.optionbar;

import vawobe.render.GridRenderer;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import static vawobe.Main.mainColor;

public class SignatureComboBox extends ComboBox<Integer> {
    public SignatureComboBox() {
        super();
        setBackground(new Background(new BackgroundFill(mainColor,new CornerRadii(5), null)));

        getItems().addAll(2, 3, 4, 5);
        setValue(4);

        setCellFactory(cell -> new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) setText(null);
                else {
                    setText(item + "/4");
                    setTextFill(Color.DARKGRAY);
                    hoverProperty().addListener((obs,oldV,newValue) -> {
                        setBackground(new Background(new BackgroundFill(newValue ? Color.ORANGE : Color.TRANSPARENT, null, null)));
                        setTextFill(newValue ? Color.WHITE : Color.DARKGRAY);
                    });
                    setDisable(false);
                    setBackground(null);
                }
            }
        });
        setButtonCell(new ListCell<Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    setText(item + "/4");
                    setTextFill(Color.WHITE);
                }
            }
        });

        valueProperty().addListener((obs,oldV,newValue) -> GridRenderer.getInstance().getSignatureProperty().set(newValue));
    }


}
