package vawobe;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import lombok.Getter;

@Getter
public class Playhead extends Line {
    private final Polygon head;

    public Playhead() {
        setStroke(Color.BLUEVIOLET);
        setStrokeWidth(2);
        setStartY(0);
        endXProperty().bind(startXProperty());

        head = new Polygon();
        head.getPoints().addAll(
                0.0, 20.0,
                -15.0, 0.0,
                15.0, 0.0
        );
        head.translateXProperty().bind(startXProperty());
        head.visibleProperty().bind(visibleProperty());
        head.setFill(Color.BLUEVIOLET);
    }
}
