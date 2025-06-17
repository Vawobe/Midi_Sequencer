package vawobe.menubar.player;

import vawobe.icons.PauseIcon;
import vawobe.icons.PlayIcon;
import vawobe.manager.PlaybackManager;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;

public class PlayButton extends MenuButton {
    public PlayButton() {
        super();
        setTooltip(new Tooltip("Play"));
        setGraphic(new PlayIcon(1));

        PlaybackManager.getInstance().getIsPlayingProperty().addListener((_, _, _) -> changeGraphic());

        setOnAction(_ -> {
            if (PlaybackManager.getInstance().isPlaying()) PlaybackManager.getInstance().pausePlayback();
            else PlaybackManager.getInstance().startPlayback();
        });
    }

    public void changeGraphic() {
        if(getGraphic() instanceof PlayIcon) {
            setGraphic(new PauseIcon(1));
            setTooltip(new Tooltip("Pause"));
        }
        else {
            setGraphic(new PlayIcon(1));
            setTooltip(new Tooltip("Play"));
        }
    }
}
