package vawobe.commands;

import vawobe.NoteView;

import java.util.List;

public abstract class NotesCommand implements SequencerCommand{
    protected final List<NoteView> noteViews;

    public NotesCommand(List<NoteView> noteViews) {
        this.noteViews = noteViews;
    }
}
