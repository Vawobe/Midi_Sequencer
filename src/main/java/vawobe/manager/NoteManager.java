package vawobe.manager;

import javafx.collections.ListChangeListener;
import vawobe.Note;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import vawobe.enums.Instruments;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class NoteManager {
    private static NoteManager instance;
    public static final String[] TONES = {"B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C"};
    public static final int[] OCTAVES = {7,6,5,4,3,2};

    @Getter private final ObservableList<Note> notesList;

    public static NoteManager getInstance() {
        if(instance == null) {
            instance = new NoteManager();
        }
        return instance;
    }

    private NoteManager() {
        notesList = FXCollections.observableArrayList();

        notesList.addListener((ListChangeListener<Note>) change -> {
            HashSet<Instruments> deletedNotesInstruments = new HashSet<>();

            while (change.next()) {
                if(change.wasRemoved()) {
                    change.getRemoved().forEach(note -> deletedNotesInstruments.add(note.getInstrument()));
                }
            }

            if(!deletedNotesInstruments.isEmpty()) {
                Set<Instruments> remaining = notesList.stream()
                        .map(Note::getInstrument)
                        .collect(Collectors.toSet());
                deletedNotesInstruments.removeAll(remaining);
                deletedNotesInstruments.forEach(instrument ->
                        MidiManager.getInstance().removeInstrument(instrument));

            }
            PlaybackManager.getInstance().updateNotes();
        });
    }

    public void addNote(Note note) {
        int channel = MidiManager.getInstance().getInstrumentChannel(note.getInstrument());
        if(channel == -1) channel = MidiManager.getInstance().addInstrument(note.getInstrument());
        if(channel != -1) {
            note.setChannel(channel);
            notesList.add(note);
            notesList.sort(Comparator.comparingDouble(Note::getColumn));
        }
    }

    public void removeNote(Note note) {
        notesList.remove(note);
    }
}
