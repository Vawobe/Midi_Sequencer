package vawobe.menubar.modes;

import vawobe.enums.Modes;
import javafx.scene.control.Tooltip;
import lombok.Getter;
import vawobe.icons.EraseIcon;

@Getter
public class EraseButton extends ModeButton {

    public EraseButton() {
        super(new EraseIcon(1), Modes.ERASE);
        setTooltip(new Tooltip("Erase"));
    }
}
