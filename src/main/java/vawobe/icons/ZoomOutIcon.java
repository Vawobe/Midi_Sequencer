package vawobe.icons;

import javafx.scene.paint.Color;

public class ZoomOutIcon extends SvgIcon{
    public ZoomOutIcon(double scale) {
        super("M7 10H13M15 15L21 21M10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10C17 13.866 13.866 17 10 17Z", scale);

        setStroke(Color.LIGHTGRAY);
        setFill(Color.TRANSPARENT);
    }
}
