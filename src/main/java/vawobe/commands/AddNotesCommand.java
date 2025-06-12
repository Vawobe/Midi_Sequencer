package vawobe.commands;

import vawobe.NoteView;
import vawobe.render.NoteRenderer;

import java.util.List;

public class AddNotesCommand extends NotesCommand{
    public AddNotesCommand(List<NoteView> noteViews) {
        super(noteViews);
    }

    @Override
    public void execute() {
        for(NoteView noteView : noteViews) NoteRenderer.getInstance().addNoteView(noteView);
    }

    @Override
    public void undo() {
        for(NoteView noteView : noteViews) NoteRenderer.getInstance().removeNoteView(noteView);
    }
}
