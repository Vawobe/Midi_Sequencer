package vawobe.menubar.modes;

import vawobe.enums.Modes;
import javafx.scene.control.Tooltip;
import vawobe.icons.SelectIcon;

public class SelectButton extends ModeButton {
    public SelectButton() {
        super(new SelectIcon(1), Modes.SELECT);
        setTooltip(new Tooltip("Select"));
    }
}
