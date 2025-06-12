package vawobe.menubar.copypaste;

import vawobe.controller.ClipboardController;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class PasteButton extends MenuButton {
    public PasteButton() {
        super();
        SVGPath icon = new SVGPath();
        icon.setContent("M10.53 18h5.6708L16.2 11.7H10.53V18ZM8.91 11.7c0-.9927.7266-1.8 1.62-1.8h4.05V4.5H12.15V6.3H5.67V4.5H3.24V16.2H8.91V11.7ZM16.2 9.9c.8934 0 1.62.8073 1.62 1.8V18c0 .9927-.7266 1.8-1.62 1.8H10.53c-.8934 0-1.62-.8073-1.62-1.8H3.24c-.8934 0-1.62-.8073-1.62-1.8V4.5c0-.9927.7266-1.8 1.62-1.8H5.67a.9.81 90 01.81-.9h4.86a.9.81 90 01.81.9h2.43c.8934 0 1.62.8073 1.62 1.8V9.9Z");
        icon.setStroke(Color.TRANSPARENT);
        icon.setFill(Color.LIGHTGRAY);
        setGraphic(icon);
        hoverProperty().addListener((_,_,newValue) -> icon.setFill(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setTooltip(new Tooltip("Paste"));

        setOnAction(_ -> pasteAction());
    }

    private void pasteAction() {
        ClipboardController.getInstance().pasteNotes(true);
    }
}
