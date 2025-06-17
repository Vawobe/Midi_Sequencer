package vawobe.commands;

import vawobe.NoteView;
import vawobe.manager.PlaybackManager;

import java.util.Map;

public class MoveNotesCommand implements SequencerCommand{
    private final Map<NoteView, Double[]> noteViews;

    public MoveNotesCommand(Map<NoteView, Double[]> noteViewMap) {
        noteViews = noteViewMap;
    }

    @Override
    public void execute() {
        boolean isPlaying = PlaybackManager.getInstance().isPlaying();
        if(isPlaying) PlaybackManager.getInstance().pausePlayback();
        for(Map.Entry<NoteView, Double[]> entry : noteViews.entrySet()) {
            entry.getKey().move(entry.getValue()[1], entry.getValue()[0]);
        }
        if(isPlaying) PlaybackManager.getInstance().startPlayback();
    }

    @Override
    public void undo() {
        boolean isPlaying = PlaybackManager.getInstance().isPlaying();
        if(isPlaying) PlaybackManager.getInstance().pausePlayback();
        for(Map.Entry<NoteView, Double[]> entry : noteViews.entrySet()) {
            entry.getKey().move(entry.getValue()[3], entry.getValue()[2]);
        }
        if(isPlaying) PlaybackManager.getInstance().startPlayback();
    }
}
