package vawobe.commands;

import vawobe.NoteView;
import vawobe.manager.PlaybackManager;
import vawobe.render.NoteRenderer;

import java.util.List;

public class AddNotesCommand extends NotesCommand{
    public AddNotesCommand(List<NoteView> noteViews) {
        super(noteViews);
    }

    @Override
    public void execute() {
        boolean isPlaying = PlaybackManager.getInstance().isPlaying();
        if(isPlaying) PlaybackManager.getInstance().pausePlayback();
        for(NoteView noteView : noteViews) NoteRenderer.getInstance().addNoteView(noteView);
        if(isPlaying) PlaybackManager.getInstance().startPlayback();
    }

    @Override
    public void undo() {
        boolean isPlaying = PlaybackManager.getInstance().isPlaying();
        if(isPlaying) PlaybackManager.getInstance().pausePlayback();
        for(NoteView noteView : noteViews) NoteRenderer.getInstance().removeNoteView(noteView);
        if(isPlaying) PlaybackManager.getInstance().startPlayback();
    }
}
