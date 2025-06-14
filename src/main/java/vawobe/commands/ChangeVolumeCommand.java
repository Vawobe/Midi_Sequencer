package vawobe.commands;

import vawobe.NoteView;

import java.util.Map;

public class ChangeVolumeCommand implements SequencerCommand {
    private final Map<NoteView, Integer[]> noteViews;

    public ChangeVolumeCommand(Map<NoteView, Integer[]> noteViews) {
        this.noteViews = noteViews;

    }

    @Override
    public void execute() {
        for(Map.Entry<NoteView, Integer[]> entry : noteViews.entrySet()) {
            entry.getKey().getViewModel().getVelocityProperty().set(entry.getValue()[0]);
            entry.getKey().getViewModel().updateNote();
        }
    }

    @Override
    public void undo() {
        for(Map.Entry<NoteView, Integer[]> entry : noteViews.entrySet()) {
            entry.getKey().getViewModel().getVelocityProperty().set(entry.getValue()[1]);
            entry.getKey().getViewModel().updateNote();
        }
    }
}
