package vawobe.menubar.copypaste;

import vawobe.icons.CopyIcon;
import vawobe.icons.SvgIcon;
import vawobe.manager.ClipboardManager;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;

public class CopyButton extends MenuButton {
    public CopyButton() {
        super();
        SvgIcon icon = new CopyIcon(1);
        setGraphic(icon);
        hoverProperty().addListener((obs,oldV,newValue) -> icon.setFill(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setTooltip(new Tooltip("Copy"));

        setOnAction(a -> copyAction());
    }

    private void copyAction() {
        ClipboardManager.getInstance().copySelectedNotes();
    }
}
