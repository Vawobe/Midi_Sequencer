package fh.swf.menubar;

import fh.swf.enums.Modes;
import javafx.scene.control.Tooltip;

public class SelectButton extends ModeButton {
    public SelectButton() {
        super("M2 2 H5 M2 2 V5 M10 2 H13 M13 2 V5 M2 10 V13 M2 13 H5 M13 10 V13 M13 13 H10", Modes.SELECT);
        setTooltip(new Tooltip("Select"));
    }
}
