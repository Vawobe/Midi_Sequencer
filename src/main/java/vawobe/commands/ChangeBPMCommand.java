package vawobe.commands;

import vawobe.manager.PlaybackManager;

public class ChangeBPMCommand implements SequencerCommand{
    private final int oldBPM;
    private final int newBPM;

    public ChangeBPMCommand(int oldBPM, int newBPM) {
        this.oldBPM = oldBPM;
        this.newBPM = newBPM;
    }

    @Override
    public void execute() {
        PlaybackManager.getInstance().getBpmProperty().set(newBPM);
        PlaybackManager.getInstance().updateNotes();
    }

    @Override
    public void undo() {
        PlaybackManager.getInstance().getBpmProperty().set(oldBPM);
        PlaybackManager.getInstance().updateNotes();
    }
}
