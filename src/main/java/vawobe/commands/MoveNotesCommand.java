package vawobe.commands;

import vawobe.NoteView;

import java.util.Map;

public class MoveNotesCommand implements SequencerCommand{
    private final Map<NoteView, Double[]> noteViews;

    public MoveNotesCommand(Map<NoteView, Double[]> noteViewMap) {
        noteViews = noteViewMap;
    }

    @Override
    public void execute() {
        for(Map.Entry<NoteView, Double[]> entry : noteViews.entrySet()) {
            entry.getKey().move(entry.getValue()[1], entry.getValue()[0]);
        }
    }

    @Override
    public void undo() {
        for(Map.Entry<NoteView, Double[]> entry : noteViews.entrySet()) {
            entry.getKey().move(entry.getValue()[3], entry.getValue()[2]);
        }
    }
}
