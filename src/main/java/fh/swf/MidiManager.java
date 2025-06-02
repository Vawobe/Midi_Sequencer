package fh.swf;

import fh.swf.enums.Instruments;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;

public class MidiManager {
    private static MidiManager instance;
    private Synthesizer synth;
    private MidiChannel channel;

    private MidiManager() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            channel = synth.getChannels()[0];
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static MidiManager getInstance() {
        if (instance == null) {
            instance = new MidiManager();
        }
        return instance;
    }

    public void playNote(int midiNote) {
        if (channel != null) {
            channel.noteOn(midiNote, 100);
        }
    }

    public void stopNote(int midiNote) {
        if (channel != null) {
            channel.noteOff(midiNote);
        }
    }

    public void close() {
        if (synth != null && synth.isOpen()) {
            synth.close();
        }
    }

    public void changeInstrument(Instruments instrument) {
        channel.programChange(instrument.getNum());
    }
    public void changeChannel(int channel) {
        if (channel >= 0 && channel < synth.getChannels().length) {
            synth.getChannels()[0] = synth.getChannels()[channel];
        }
    }

    public void noteOn(int note, int velocity) {
        synth.getChannels()[0].noteOn(note, velocity); // Kanal 0 ist Standard
    }

    public void noteOff(int note) {
        synth.getChannels()[0].noteOff(note);
    }

    public void setVolume(int volume) {
        if(channel != null) {
            channel.controlChange(7, volume);
        }
    }

    public void playNoteFor(int midiNote, int velocity, Duration duration) {
        if(channel != null) {
            channel.noteOn(midiNote, velocity);

            PauseTransition pause = new PauseTransition(duration);
            pause.setOnFinished(_ -> channel.noteOff(midiNote));
            pause.play();
        }
    }
}

