package vawobe.menubar.modes;

import vawobe.enums.Modes;
import javafx.scene.control.Tooltip;
import vawobe.icons.DrawIcon;

public class DrawButton extends ModeButton {
    public DrawButton() {
        super(new DrawIcon(1), Modes.DRAW);
        setTooltip(new Tooltip("Draw"));
    }
}
