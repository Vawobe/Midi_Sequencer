package vawobe.manager;

import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import vawobe.KeyBox;
import vawobe.KeyButton;
import vawobe.Note;
import vawobe.enums.Instruments;
import lombok.Getter;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Synthesizer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static vawobe.Main.mainPane;

public class MidiManager {
    private static MidiManager instance;

    private final Set<Note> activeNotes = ConcurrentHashMap.newKeySet();
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
        if(channelIsValid(note.getChannel())) {
            KeyBox keyBox = mainPane.getPianoGridPane().getCurrentKeyBox();
            Button button = (Button) keyBox.getChildren().get(note.getRow());
            button.setBackground(new Background(new BackgroundFill(note.getInstrument().getColor(), null, null)));

            channels[note.getChannel()].noteOn(note.getMidiNote(), note.getVelocity());
            activeNotes.add(note);
        }
    }

    public void stopNote(Note note) {
        if(channelIsValid(note.getChannel())) {
            KeyBox keyBox = mainPane.getPianoGridPane().getCurrentKeyBox();
            KeyButton button = (KeyButton) keyBox.getChildren().get(note.getRow());
            button.setRightBackground();
            channels[note.getChannel()].noteOff(note.getMidiNote());
            activeNotes.remove(note);
        }
    }

    public void close() {
        if (synth != null && synth.isOpen()) {
            synth.close();
        }
    }

    public int addInstrument(Instruments instrument) {
        int channel = (instrumentToChannel.getOrDefault(instrument, -1));
        if(channel == -1) {
            if(instrument == Instruments.DRUMS) channel = 9;
            else {
                channel = getNextFreeChannel();
                changeInstrument(instrument, channel);
            }
            if(channel != -1) {
                instrumentToChannel.put(instrument, channel);
            }
        }
        return channel;
    }

    private int getNextFreeChannel() {
        if(freeChannels.isEmpty()) return -1;

        Collections.sort(freeChannels);
        int nextFreeChannel = freeChannels.get(0);
        freeChannels.remove(0);
        return nextFreeChannel;
    }

    private void changeInstrument(Instruments instrument, int channel) {
        if(channelIsValid(channel))
            channels[channel].programChange(instrument.getNum());
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
        if(instrumentToChannel.get(instrument) != null) {
            int channel = instrumentToChannel.get(instrument);
            instrumentToChannel.remove(instrument);
            freeChannels.add(channel);
        }
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

    public void playDemoDrumTone(int note) {
        synth.getChannels()[9].noteOn(note,100);
    }
    public void stopDemoDrumTone(int note) {
        synth.getChannels()[9].noteOff(note);
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

    private boolean channelIsValid(int channel) {
        return channel >= 0 && channel <= 15;
    }

    public void allNotesOff() {
        for(Note note : activeNotes) {
            stopNote(note);
        }
    }
}

