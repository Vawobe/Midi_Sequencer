package vawobe.commands;

import vawobe.NoteView;
import vawobe.manager.PlaybackManager;
import vawobe.manager.NoteManager;
import vawobe.render.GridRenderer;
import vawobe.render.NoteRenderer;

import java.util.List;

import static vawobe.Main.mainPane;

public class LoadCommand implements SequencerCommand {
    private final List<NoteView> loadedNotes;
    private final List<NoteView> oldNotes;

    private final int newBPM;
    private final int oldBPM;
    private final int newSignature;
    private final int oldSignature;

    private final String newName;
    private final String oldName;

    public LoadCommand(List<NoteView> loadedNotes, List<NoteView> oldNotes, int newBPM, int oldBPM, int newSignature, int oldSignature, String newName, String oldName) {
        this.loadedNotes = loadedNotes;
        this.oldNotes = oldNotes;
        this.newBPM = newBPM;
        this.oldBPM = oldBPM;
        this.newSignature = newSignature;
        this.oldSignature = oldSignature;
        this.newName = newName;
        this.oldName = oldName;
    }

    @Override
    public void execute() {
        NoteManager.getInstance().getNotesList().clear();

        mainPane.getMenuBar().getTitleBox().getTitleTextField().setText(newName);
        PlaybackManager.getInstance().getBpmProperty().set(newBPM);
        GridRenderer. getInstance().getSignatureProperty().set(newSignature);
        for(NoteView noteView : loadedNotes) NoteRenderer.getInstance().addNoteView(noteView);
        PlaybackManager.getInstance().updateNotes();

    }

    @Override
    public void undo() {
        NoteManager.getInstance().getNotesList().clear();

        mainPane.getMenuBar().getTitleBox().getTitleTextField().setText(oldName);
        PlaybackManager.getInstance().getBpmProperty().set(oldBPM);
        GridRenderer.getInstance().getSignatureProperty().set(oldSignature);
        for(NoteView noteView : oldNotes) NoteRenderer.getInstance().addNoteView(noteView);
        PlaybackManager.getInstance().updateNotes();
    }
}
