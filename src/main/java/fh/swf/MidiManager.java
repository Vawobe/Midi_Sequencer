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
    private MidiChannel[] channels;

    private MidiManager() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();
            channels = synth.getChannels();
        } catch (Exception ignored) { }
    }

    public static MidiManager getInstance() {
        if (instance == null) {
            instance = new MidiManager();
        }
        return instance;
    }

    public void playNote(int midiNote, int velocity, int channel) {
        channels[channel].noteOn(midiNote, velocity);
    }

    public void stopNote(int midiNote, int channel) {
        channels[channel].noteOff(midiNote);
    }

    public void close() {
        if (synth != null && synth.isOpen()) {
            synth.close();
        }
    }

    public void changeInstrument(Instruments instrument, int channel) {
        if(channel >= 0 && channel < channels.length) {
            channels[channel].programChange(instrument.getNum());
        }
    }

    public void noteOn(int note, int velocity) {
        synth.getChannels()[0].noteOn(note, velocity); // Kanal 0 ist Standard
    }

    public void noteOff(int note) {
        synth.getChannels()[0].noteOff(note);
    }

    public void setVolume(int volume) {
        for(MidiChannel channel : channels) {
            if (channel != null) {
                channel.controlChange(7, volume);
            }
        }
    }

    public void changeDemoChannel(Instruments instrument) {
        changeInstrument(instrument, 0);
    }
}

