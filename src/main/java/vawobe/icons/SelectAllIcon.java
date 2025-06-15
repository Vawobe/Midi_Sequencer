package vawobe.icons;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectAllIcon extends Rectangle {
    public SelectAllIcon(double scale) {
        super(15, 15);
        setStroke(Color.LIGHTGRAY);
        setFill(Color.TRANSPARENT);
        getStrokeDashArray().addAll(1.0,2.0);

        setScaleX(scale);
        setScaleY(scale);
    }
}
