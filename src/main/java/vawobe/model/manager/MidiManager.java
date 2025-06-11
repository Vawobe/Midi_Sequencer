package vawobe.model.manager;

import vawobe.Note;
import vawobe.enums.Instruments;
import lombok.Getter;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class MidiManager {
    private static MidiManager instance;

    private Synthesizer synth;
    @Getter private MidiChannel[] channels;
    @Getter private static final ArrayList<Integer> freeChannels = new ArrayList<>();
    @Getter private static final HashMap<Instruments, Integer> instrumentToChannel = new HashMap<>();


    private MidiManager() {
        try {
            synth = MidiSystem.getSynthesizer();
            synth.open();

            channels = synth.getChannels();

            for(int i = 1; i <= 15; i++)
                if(i != 10) freeChannels.add(i);

        } catch (Exception ignored) { }
    }

    public static MidiManager getInstance() {
        if (instance == null) {
            instance = new MidiManager();
        }
        return instance;
    }

    public void playNote(Note note){
        if(note.getChannel() > 0 && note.getChannel() < 15)
            channels[note.getChannel()].noteOn(note.getMidiNote(), note.getVelocity());
    }

    public void stopNote(Note note) {
        channels[note.getChannel()].noteOff(note.getMidiNote());
    }

    public void close() {
        if (synth != null && synth.isOpen()) {
            synth.close();
        }
    }

    public int addInstrument(Instruments instrument) {
        int channel = (instrumentToChannel.getOrDefault(instrument, -1));
        if(channel == -1) {
            channel = getNextFreeChannel();
            if(channel != -1) {
                instrumentToChannel.put(instrument, channel);
                changeInstrument(instrument, channel);
            }
        }
        return channel;
    }

    private int getNextFreeChannel() {
        if(freeChannels.isEmpty()) return -1;

        Collections.sort(freeChannels);
        int nextFreeChannel = freeChannels.getFirst();
        freeChannels.removeFirst();
        return nextFreeChannel;
    }

    private void changeInstrument(Instruments instrument, int channel) {
        if(channel >= 0 && channel < 16) {
            channels[channel].programChange(instrument.getNum());
        }
    }

    public int getInstrumentChannel(Instruments instrument) {
        for(int i = 1; i < channels.length; i++) {
            if(channels[i].getProgram() == instrument.getNum()) {
                return i;
            }
        }
        return -1;
    }

    public void removeInstrument(Instruments instrument) {
        int channel = instrumentToChannel.get(instrument);
        instrumentToChannel.remove(instrument);
        freeChannels.add(channel);
    }

    public void changeDemoChannel(Instruments instrument) {
        changeInstrument(instrument, 0);
    }

    public void setVolume(int volume) {
        for(MidiChannel channel : channels) {
            if (channel != null) {
                channel.controlChange(7, volume);
            }
        }
    }

    public void playDemoTone(int note) {
        synth.getChannels()[0].noteOn(note, 100);
    }

    public void stopDemoTone(int note) {
        synth.getChannels()[0].noteOff(note);
    }

    public void removeInstrumentIfNecessary(Instruments instrument) {
        for(Note note : NoteManager.getInstance().getNotesList()) {
            if(note.getInstrument() == instrument) return;
        }
        removeInstrument(instrument);
    }
}

