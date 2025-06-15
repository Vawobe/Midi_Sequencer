package vawobe.icons;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class PauseIcon extends HBox {
    public PauseIcon(double scale) {
        super(4);
        Rectangle leftBar = new Rectangle(5, 15);
        leftBar.setFill(Color.ORANGE);
        Rectangle rightBar = new Rectangle(5, 15);
        rightBar.setFill(Color.ORANGE);
        getChildren().addAll(leftBar, rightBar);

        setScaleX(scale);
        setScaleY(scale);
    }
}
