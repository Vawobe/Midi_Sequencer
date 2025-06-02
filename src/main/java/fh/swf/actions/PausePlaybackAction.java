package fh.swf.actions;

import static fh.swf.Main.mainPane;

public class PausePlaybackAction implements Action {
    @Override
    public void use() {
        mainPane.getPianoPane().getPianoGrid().pausePlayback();
    }
}
