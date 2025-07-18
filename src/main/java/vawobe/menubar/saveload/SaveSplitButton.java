package vawobe.menubar.saveload;

import javafx.scene.control.*;
import javafx.scene.shape.SVGPath;
import vawobe.Note;
import vawobe.icons.SaveIcon;
import vawobe.manager.PlaybackManager;
import vawobe.manager.NoteManager;
import vawobe.render.GridRenderer;
import vawobe.save.ProjectIO;

import java.util.ArrayList;
import java.util.List;

public class SaveSplitButton extends DropDownButton {

    public SaveSplitButton() {
        super();
        SVGPath icon = new SaveIcon(1);
        setGraphic(icon);
        setTooltip(new Tooltip("Save"));
        setOnAction(_ -> save());

        Button saveMidiFX = new Button("Save File");
        saveMidiFX.setOnAction(_ -> save());
        Button exportMidi = new Button("Export MIDI");
        exportMidi.setOnAction(_ -> exportMidi());
        Button exportWAV = new Button("Export WAV");
        exportWAV.setOnAction(_ -> exportWav());
        Button exportMP3 = new Button("Export MP3");
        exportMP3.setOnAction(_ -> exportMp3());

        addItems(saveMidiFX, exportMidi, exportWAV, exportMP3);
    }

    private void save() {
        List<Note> notes = new ArrayList<>(NoteManager.getInstance().getNotesList());
        if(!notes.isEmpty()) {
            int bpm = PlaybackManager.getInstance().getBpmProperty().get();
            int signature = GridRenderer.getInstance().getSignatureProperty().get();
            ProjectIO.saveProject(bpm, signature, notes);
        }
    }

    private void exportMidi() {
        List<Note> notes = new ArrayList<>(NoteManager.getInstance().getNotesList());
        if(!notes.isEmpty()) ProjectIO.exportMidi(notes);

    }

    private void exportWav() {
        List<Note> notes = new ArrayList<>(NoteManager.getInstance().getNotesList());
        if(!notes.isEmpty()) ProjectIO.exportWav(notes);
    }

    private void exportMp3() {
        List<Note> notes = new ArrayList<>(NoteManager.getInstance().getNotesList());
        if(!notes.isEmpty()) ProjectIO.exportMP3(notes);
    }
}
