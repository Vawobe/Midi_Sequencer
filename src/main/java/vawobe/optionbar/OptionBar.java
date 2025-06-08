package vawobe.optionbar;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;

import static vawobe.Main.mainColor;

public class OptionBar extends HBox {
    public OptionBar() {
        setSpacing(15);
        setPadding(new Insets(5));
        setBackground(new Background(new BackgroundFill(mainColor, null, null)));

        getChildren().addAll(
                new GridBox(),
                new SignatureBox()
        );
    }
}
