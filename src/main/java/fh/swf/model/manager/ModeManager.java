package fh.swf.model.manager;

import fh.swf.enums.Modes;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

public class ModeManager {
    private static ModeManager instance;

    @Getter private final SimpleObjectProperty<Modes> currentModeProperty;

    public static ModeManager getInstance() {
        if(instance == null) {
            instance = new ModeManager();
        }
        return instance;
    }

    private ModeManager() {
        currentModeProperty = new SimpleObjectProperty<>(Modes.DRAW);
    }

    public void changeMode(Modes mode) {
        currentModeProperty.set(mode);
    }
}
