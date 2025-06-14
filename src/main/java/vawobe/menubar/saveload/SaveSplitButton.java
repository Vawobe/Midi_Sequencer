package vawobe.menubar.saveload;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import vawobe.Note;
import vawobe.manager.PlaybackManager;
import vawobe.manager.NoteManager;
import vawobe.render.GridRenderer;
import vawobe.save.ProjectIO;

import java.util.ArrayList;
import java.util.List;

public class SaveSplitButton extends DropDownButton {

    public SaveSplitButton() {
        super();
        SVGPath icon = getSvgPath();
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

    private SVGPath getSvgPath() {
        SVGPath icon = new SVGPath();
        icon.setContent("M6.336 1.584V3.96c0 .792.792 1.584 1.584 1.584h3.168c.792 0 1.584-.792 1.584-1.584V1.584Zm-1.584 15.84V11.88c0-.792.792-1.584 1.584-1.584h6.336c.792 0 1.584.792 1.584 1.584v5.544ZM1.584 9.504V3.168c0-.792.792-1.584 1.584-1.584h10.296s.792 0 1.584.792l1.584 1.584c.792.792.792 1.584.792 1.584V15.84c0 .792-.792 1.584-1.584 1.584H3.168c-.792 0-1.584-.792-1.584-1.584Z");
        icon.setStroke(Color.LIGHTBLUE);
        icon.setFill(Color.TRANSPARENT);
        return icon;
    }
}
