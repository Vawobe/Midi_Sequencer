package vawobe.menubar.copypaste;

import vawobe.icons.CutIcon;
import vawobe.manager.ClipboardManager;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;


public class CutButton extends MenuButton {
    public CutButton() {
        super();
        CutIcon icon = new CutIcon(1);
        setGraphic(icon);
        hoverProperty().addListener((obs,oldV,newValue) -> icon.setFill(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setTooltip(new Tooltip("Cut"));

        setOnAction(a -> cutAction());
    }

    private void cutAction() {
        ClipboardManager.getInstance().cutSelectedNotes();
    }
}
