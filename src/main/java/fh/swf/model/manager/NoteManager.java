package fh.swf.model.manager;

import fh.swf.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;

import java.util.Comparator;

public class NoteManager {
    private static NoteManager instance;
    public static final String[] TONES = {"B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C"};
    public static final int[] OCTAVES = {7,6,5,4,3,2};

    @Getter private final ObservableList<Note> notes;

    public static NoteManager getInstance() {
        if(instance == null) {
            instance = new NoteManager();
        }

        return instance;
    }

    private NoteManager() {
        notes = FXCollections.observableArrayList();
    }

    public void addNote(Note note) {
        notes.add(note);
        notes.sort(Comparator.comparingDouble(Note::getColumn));
    }

    public void removeNote(Note note) {
        notes.remove(note);
    }
}
