package vawobe.menubar.saveload;

import vawobe.SaveIcon;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;

public class SaveButton extends MenuButton {
    public SaveButton() {
        super();
        setTooltip(new Tooltip("Speichern"));
        SaveIcon saveIcon = new SaveIcon();
        setGraphic(saveIcon);

        setOnAction(_ -> {

        });
    }
}
