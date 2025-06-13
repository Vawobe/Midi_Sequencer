package vawobe.commands;

import vawobe.NoteView;
import vawobe.manager.SelectionManager;

import java.util.Set;

public class SelectNotesCommand implements SequencerCommand {
    private final Set<NoteView> oldSelection;
    private final Set<NoteView> newSelection;

    public SelectNotesCommand(Set<NoteView> oldSelection, Set<NoteView> newSelection) {
        this.oldSelection = oldSelection;
        this.newSelection = newSelection;
    }

    @Override
    public void execute() {
        SelectionManager.getInstance().getSelectedNotes().clear();
        SelectionManager.getInstance().getSelectedNotes().addAll(newSelection);
    }

    @Override
    public void undo() {
        SelectionManager.getInstance().getSelectedNotes().clear();
        SelectionManager.getInstance().getSelectedNotes().addAll(oldSelection);
    }
}
