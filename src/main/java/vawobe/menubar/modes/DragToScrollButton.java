package vawobe.menubar.modes;

import javafx.scene.control.Tooltip;
import vawobe.enums.Modes;

public class DragToScrollButton extends ModeButton {
    public DragToScrollButton() {
        super("M8 11 6 12 4 11M8 1 6 0 4 1m7 7 1-2-1-2M1 8 0 6 1 4m5 8V0m6 6H0", Modes.DRAG_TO_SCROLL);
        setTooltip(new Tooltip("Drag to scroll"));
    }
}
