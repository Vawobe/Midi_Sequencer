package fh.swf;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Playhead extends Line {
    public Playhead() {
        setStroke(Color.BLUEVIOLET);
        setStrokeWidth(2);
        setStartY(0);
        endXProperty().bind(startXProperty());
    }
}
