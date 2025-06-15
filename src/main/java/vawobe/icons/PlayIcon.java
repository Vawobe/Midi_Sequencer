package vawobe.icons;

import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class PlayIcon extends Polygon {
    public PlayIcon(double scale) {
        getPoints().addAll(
                0.0, 0.0,
                15.0, 7.5,
                0.0, 15.0
        );
        setFill(Color.ORANGE);
        setScaleX(scale);
        setScaleY(scale);
    }
}
