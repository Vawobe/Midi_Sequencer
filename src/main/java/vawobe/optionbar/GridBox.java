package vawobe.optionbar;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class GridBox extends HBox {
    private final GridComboBox gridComboBox;

    public GridBox() {
        Label label = new Label("Grid");
        label.setTextFill(Color.LIGHTGRAY);

        gridComboBox = new GridComboBox();

        getChildren().addAll(label, gridComboBox);
        setAlignment(Pos.BASELINE_LEFT);
        setBorder(new Border(new BorderStroke(
                Color.WHITE,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(0, 0, 0.5, 0)
        )));
    }
}
