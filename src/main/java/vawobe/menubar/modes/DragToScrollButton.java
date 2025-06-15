package vawobe.menubar.modes;

import javafx.scene.control.Tooltip;
import vawobe.enums.Modes;
import vawobe.icons.DragToScrollIcon;

public class DragToScrollButton extends ModeButton {
    public DragToScrollButton() {
        super(new DragToScrollIcon(1), Modes.DRAG_TO_SCROLL);
        setTooltip(new Tooltip("Drag to scroll"));
    }
}
