package vawobe.save;

import vawobe.Note;
import vawobe.enums.Instruments;
import vawobe.manager.PlaybackManager;

import javax.sound.midi.*;
import java.io.File;
import java.util.*;

public class MidiIO {
    private final static int RESOLUTION = 960;

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

    public static Sequence createMidiSequence(List<Note> notes) throws Exception {
        int resolution = 960;
        Sequence sequence = new Sequence(Sequence.PPQ, resolution);
        Track track = sequence.createTrack();

        MetaMessage tempoMessage = new MetaMessage();
        int tempo = 60000000 / PlaybackManager.getInstance().getBpmProperty().get();
        byte[] data = new byte[3];
        data[0] = (byte) ((tempo >> 16) & 0xFF);
        data[1] = (byte) ((tempo >> 8) & 0xFF);
        data[2] = (byte) (tempo & 0xFF);
        tempoMessage.setMessage(0x51, data, 3);
        MidiEvent tempoEvent = new MidiEvent(tempoMessage, 0);
        track.add(tempoEvent);

        Set<Integer> channelsSet = new HashSet<>();
        for (Note note : notes) {
            int channel = note.getChannel();
            if (!channelsSet.contains(channel)) {
                ShortMessage programChange = new ShortMessage();
                programChange.setMessage(ShortMessage.PROGRAM_CHANGE, channel, note.getInstrument().getNum(), 0);
                MidiEvent programChangeEvent = new MidiEvent(programChange, 0);
                track.add(programChangeEvent);
                channelsSet.add(channel);
            }
        }

        for (Note note : notes) {
            ShortMessage on = new ShortMessage();
            on.setMessage(ShortMessage.NOTE_ON, note.getChannel(), note.getMidiNote(), note.getVelocity());

            ShortMessage off = new ShortMessage();
            off.setMessage(ShortMessage.NOTE_OFF, note.getChannel(), note.getMidiNote(), 0);

            long tickOn = (long) (note.getColumn() * resolution);
            long tickOff = (long) (tickOn + (note.getLength() * resolution));

            MidiEvent noteOn = new MidiEvent(on, tickOn);
            MidiEvent noteOff = new MidiEvent(off, tickOff);

            track.add(noteOn);
            track.add(noteOff);
        }

        return sequence;
    }

}
