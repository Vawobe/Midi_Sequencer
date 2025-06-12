package vawobe.menubar.copypaste;

import vawobe.controller.ClipboardController;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class CopyButton extends MenuButton {
    public CopyButton() {
        super();
        SVGPath icon = new SVGPath();
        icon.setContent("M6 9V19.5h8.25V9H6Zm9.75 6.75H18V5.25H9.75V7.5H15l.75.75v7.5Zm3.75.75-.75.75h-3v3L15 21H5.25l-.75-.75v-12l.75-.75h3v-3L9 3.75h9.75l.75.75v12Z");
        icon.setStroke(Color.TRANSPARENT);
        icon.setFill(Color.LIGHTGRAY);
        setGraphic(icon);
        hoverProperty().addListener((_,_,newValue) -> icon.setFill(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setTooltip(new Tooltip("Copy"));

        setOnAction(_ -> copyAction());
    }

    private void copyAction() {
        ClipboardController.getInstance().copySelectedNotes();
    }
}
