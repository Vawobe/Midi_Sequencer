package vawobe.menubar.player;

import vawobe.icons.StopIcon;
import vawobe.manager.PlaybackManager;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;

public class StopButton extends MenuButton {
    public StopButton() {
        super();
        setTooltip(new Tooltip("Stop"));
        setGraphic(new StopIcon(1));

        setOnAction(a -> PlaybackManager.getInstance().stopPlayback());
    }
}
