package vawobe.menubar.copypaste;

import vawobe.icons.PasteIcon;
import vawobe.manager.ClipboardManager;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

public class PasteButton extends MenuButton {
    public PasteButton() {
        super();
        PasteIcon icon = new PasteIcon(1);
        setGraphic(icon);
        hoverProperty().addListener((_,_,newValue) -> icon.setFill(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setTooltip(new Tooltip("Paste"));

        setOnAction(_ -> pasteAction());
    }

    private void pasteAction() {
        ClipboardManager.getInstance().pasteNotes(true);
    }
}
