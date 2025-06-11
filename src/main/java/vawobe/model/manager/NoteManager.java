package vawobe.model.manager;

import javafx.collections.ListChangeListener;
import vawobe.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import vawobe.NoteView;
import vawobe.controller.PlaybackController;
import vawobe.enums.Instruments;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;

public class NoteManager {
    private static NoteManager instance;
    public static final String[] TONES = {"B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C"};
    public static final int[] OCTAVES = {7,6,5,4,3,2};

    @Getter private final ObservableList<Note> notes;
    @Getter private final ArrayList<NoteView> selectedNotes;

    public static NoteManager getInstance() {
        if(instance == null) {
            instance = new NoteManager();
        }
        return instance;
    }

    private NoteManager() {
        selectedNotes = new ArrayList<>();
        notes = FXCollections.observableArrayList();

        notes.addListener((ListChangeListener<Note>) change -> {
            HashSet<Instruments> deletedNotesInstruments = new HashSet<>();
            while (change.next()) {
                if(change.wasRemoved()) {
                    change.getRemoved().forEach(note -> deletedNotesInstruments.add(note.getInstrument()));
                    notes.forEach(note -> deletedNotesInstruments.remove(note.getInstrument()));
                    deletedNotesInstruments.forEach(instrument -> MidiManager.getInstance().removeInstrument(instrument));
                }
            }
            PlaybackController.getInstance().updateNotes();
        });
    }

    public void addNote(Note note) {
        int channel = MidiManager.getInstance().getInstrumentChannel(note.getInstrument());
        if(channel == -1) channel = MidiManager.getInstance().addInstrument(note.getInstrument());
        if(channel != -1) {
            note.setChannel(channel);
            notes.add(note);
            notes.sort(Comparator.comparingDouble(Note::getColumn));
        }
    }

    public void removeNote(Note note) {
        notes.remove(note);
    }
}
