package vawobe.optionbar;

import vawobe.render.GridRenderer;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import static vawobe.Main.mainColor;

public class GridComboBox extends ComboBox<Integer> {
    public GridComboBox() {
        super();
        setBackground(new Background(new BackgroundFill(mainColor,new CornerRadii(5), null)));

        getItems().addAll(4, 8, 12, 16, 24, 32, 48, 64);
        setValue(4);

        setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) setText(null);
                else {
                    setText(item == 4 ? "1" : "1/" + item / 4);
                    setTextFill(Color.DARKGRAY);
                    hoverProperty().addListener((_,_,newValue) -> {
                        setBackground(new Background(new BackgroundFill(newValue ? Color.ORANGE : Color.TRANSPARENT, null, null)));
                        setTextFill(newValue ? Color.WHITE : Color.DARKGRAY);
                    });
                    setDisable(false);
                    setBackground(null);
                }
            }
        });
        setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) setText(null);
                else {
                    setText(item == 4 ? "1" : "1/" + item / 4);
                    setTextFill(Color.WHITE);
                }
            }
        });

        valueProperty().addListener((_,_,newValue) -> GridRenderer.getInstance().getGridProperty().set(newValue));
    }
}
