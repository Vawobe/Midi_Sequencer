package vawobe.menubar.zoom;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ZoomBox extends HBox {
    public ZoomBox() {
        super();
        getChildren().addAll(new ZoomOutButton(), new ZoomInButton());

        setBorder(new Border(new BorderStroke(
                Color.LIGHTGRAY,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(0, 0.5, 0, 0)
        )));
    }
}
