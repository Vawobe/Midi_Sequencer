package fh.swf;

import fh.swf.render.GridRenderer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import static fh.swf.render.GridRenderer.CELL_WIDTH;

public class Playhead extends Line {
    public Playhead() {
        setStroke(Color.RED);
        setStrokeWidth(2);
        setStartY(0);
        endXProperty().bind(startXProperty());
    }

    public int getCurrentBeat() {
        double x = getStartX();
        return (int) (x / (CELL_WIDTH * GridRenderer.zoom));
    }
}
