package fh.swf.menubar;

import fh.swf.enums.Modes;
import javafx.scene.control.Tooltip;
import lombok.Getter;

@Getter
public class EraseButton extends ModeButton {

    public EraseButton() {
        super("M3 12L12 3L9 0L0 9L3 12Z M3 12H9", Modes.ERASE);
        setTooltip(new Tooltip("Erase"));
    }
}
