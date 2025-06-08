package vawobe.menubar.copypaste;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class CopyPasteButtonBox extends HBox {
    public CopyPasteButtonBox() {
        getChildren().addAll(
                new CutButton(),
                new CopyButton(),
                new PasteButton(),
                new SelectAllButton()
        );

        setBorder(new Border(new BorderStroke(
                Color.LIGHTGRAY,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(0, 0.5, 0, 0.5)
        )));
    }

}
