package vawobe.icons;

import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public abstract class SvgIcon extends SVGPath {
    public SvgIcon(String content, double scale) {
        setContent(content);
        setFill(Color.TRANSPARENT);
        setStroke(Color.DARKGRAY);
        setStrokeWidth(1);

        setScaleX(scale);
        setScaleY(scale);
    }
}
