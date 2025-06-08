package vawobe.menubar.modes;

import vawobe.enums.Modes;
import javafx.scene.control.Tooltip;

public class DrawButton extends ModeButton {
    public DrawButton() {
        super("M0 0-2.1213-2.1213-7.0711 2.8285-8.8389 6.7175-4.9498 4.9498l6.364-6.364L-.7071-3.5355-2.1213-2.1213ZM-9 7-5 7Z", Modes.DRAW);
        setTooltip(new Tooltip("Draw"));
    }
}
