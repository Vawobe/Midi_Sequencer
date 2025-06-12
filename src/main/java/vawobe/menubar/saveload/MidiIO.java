package vawobe.menubar.saveload;

import vawobe.Note;
import vawobe.enums.Instruments;

import javax.sound.midi.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MidiIO {
    private final static int RESOLUTION = 480;

    public static Map<Integer, List<Note>> importMidi(File midiFile) throws Exception {
        Map<Integer, List<Note>> notesPerTrack = new HashMap<>();
        Sequence sequence = MidiSystem.getSequence(midiFile);
        for (Track track : sequence.getTracks()) {
            List<Note> notes = new ArrayList<>();
            double resolution = sequence.getResolution();

            Map<Integer, MidiEvent> activesNotes = new HashMap<>();

            int instrument = 0;
            for(int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                if (message instanceof ShortMessage sm) {
                    int command = sm.getCommand();
                    int key = sm.getData1();
                    int velocity = sm.getData2();
                    long tick = event.getTick();

                    if (command == ShortMessage.NOTE_ON && velocity > 0) {
                        activesNotes.put(key, event);
                    } else if ((command == ShortMessage.NOTE_OFF) ||
                            (command == ShortMessage.NOTE_ON && velocity == 0)) {
                        MidiEvent onEvent = activesNotes.remove(key);
                        if (onEvent != null) {
                            long startTick = onEvent.getTick();
                            double length = (tick - startTick) / resolution;
                            double column = startTick / resolution;
                            int row = 107 - key - 12;

                            Note note = new Note(column, row, length, 1, Instruments.ACOUSTIC_GRAND_PIANO);
                            note.setVelocity(100);
                            notes.add(note);
                        }
                    } else if((command == ShortMessage.PROGRAM_CHANGE)) {
                        instrument = sm.getData1();
                    }
                }
            }
            notesPerTrack.computeIfAbsent(instrument, _ -> new ArrayList<>()).addAll(notes);
        }
        return notesPerTrack;
    }

    public static void exportMidi(List<Note> notes, String fileName) throws Exception {
        Sequence sequence = new Sequence(Sequence.PPQ, RESOLUTION);
        Track track = sequence.createTrack();

        for(Note note : notes) {
            ShortMessage on = new ShortMessage();
            on.setMessage(ShortMessage.NOTE_ON, 0, note.getMidiNote(), note.getVelocity());

            ShortMessage off = new ShortMessage();
            off.setMessage(ShortMessage.NOTE_OFF, 0, note.getMidiNote(), 0);

            long tickOn = (long) note.getColumn() * RESOLUTION;
            long tickOff = (long) (tickOn + (note.getLength() * RESOLUTION));

            MidiEvent noteOn = new MidiEvent(on, tickOn);
            MidiEvent noteOff = new MidiEvent(off, tickOff);

            track.add(noteOn);
            track.add(noteOff);

        }

        File midiFile = new File(fileName);
        MidiSystem.write(sequence, 1, midiFile);
    }
}
