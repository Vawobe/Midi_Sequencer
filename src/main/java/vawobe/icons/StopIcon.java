package vawobe.icons;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StopIcon extends Rectangle {
    public StopIcon(double scale) {
        super(15,15);
        setFill(Color.ORANGE);
        setScaleX(scale);
        setScaleY(scale);
    }
}
