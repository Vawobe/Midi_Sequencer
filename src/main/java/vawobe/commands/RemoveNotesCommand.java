package vawobe.commands;

import vawobe.NoteView;
import vawobe.render.NoteRenderer;

import java.util.List;

public class RemoveNotesCommand extends NotesCommand {
    public RemoveNotesCommand(List<NoteView> noteViews) {
        super(noteViews);
    }

    @Override
    public void execute() {
        for(NoteView noteView : noteViews) NoteRenderer.getInstance().removeNoteView(noteView);
    }

    @Override
    public void undo() {
        for(NoteView noteView : noteViews) NoteRenderer.getInstance().addNoteView(noteView);
    }
}
