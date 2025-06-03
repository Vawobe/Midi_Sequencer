package fh.swf.optionbar;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class OptionBar extends HBox {
    public OptionBar() {
        setSpacing(15);
        setPadding(new Insets(5));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        getChildren().addAll(
                new GridComboBox(),
                new SignatureComboBox()
        );
    }
}
