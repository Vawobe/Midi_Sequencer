package vawobe.commands;

import vawobe.controller.PlaybackController;

public class ChangeBPMCommand implements SequencerCommand{
    private final int oldBPM;
    private final int newBPM;

    public ChangeBPMCommand(int oldBPM, int newBPM) {
        this.oldBPM = oldBPM;
        this.newBPM = newBPM;
    }

    @Override
    public void execute() {
        PlaybackController.getInstance().getBpmProperty().set(newBPM);
        PlaybackController.getInstance().updateNotes();
    }

    @Override
    public void undo() {
        PlaybackController.getInstance().getBpmProperty().set(oldBPM);
        PlaybackController.getInstance().updateNotes();
    }
}
