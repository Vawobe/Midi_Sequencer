package vawobe.commands;

import vawobe.NoteView;
import vawobe.enums.Instruments;
import vawobe.manager.MidiManager;
import vawobe.manager.PlaybackManager;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ChangeInstrumentCommand implements SequencerCommand {
    private final Map<NoteView, Instruments> oldInstruments;
    private final Instruments newInstrument;

    public ChangeInstrumentCommand(Map<NoteView, Instruments> oldInstruments, Instruments newInstrument) {
        this.oldInstruments = oldInstruments;
        this.newInstrument = newInstrument;
    }


    @Override
    public void execute() {
        int newChannel = MidiManager.getInstrumentToChannel().getOrDefault(newInstrument, -1);
        if(newChannel == -1) newChannel = MidiManager.getInstance().addInstrument(newInstrument);
        if(newChannel != -1) {
            Set<Instruments> oldIns = new HashSet<>();
            for(Map.Entry<NoteView, Instruments> entry : oldInstruments.entrySet()) {
                if(PlaybackManager.getInstance().isPlaying()) MidiManager.getInstance().stopNote(entry.getKey().getViewModel().getNote());

                entry.getKey().getViewModel().getInstrumentProperty().set(newInstrument);
                entry.getKey().getViewModel().getChannelProperty().set(newChannel);
                entry.getKey().getViewModel().updateNote();
                oldIns.add(entry.getValue());
            }
            oldIns.forEach(instrument -> MidiManager.getInstance().removeInstrumentIfNecessary(instrument));
        }
    }

    @Override
    public void undo() {
        for(Map.Entry<NoteView, Instruments> entry : oldInstruments.entrySet()) {
            int newChannel = MidiManager.getInstrumentToChannel().getOrDefault(entry.getValue(), -1);
            if(newChannel == -1) newChannel = MidiManager.getInstance().addInstrument(entry.getValue());
            if(newChannel != -1) {
                if(PlaybackManager.getInstance().isPlaying()) MidiManager.getInstance().stopNote(entry.getKey().getViewModel().getNote());
                entry.getKey().getViewModel().getInstrumentProperty().set(entry.getValue());
                entry.getKey().getViewModel().getChannelProperty().set(newChannel);
                entry.getKey().getViewModel().updateNote();

            }
        }
        MidiManager.getInstance().removeInstrumentIfNecessary(newInstrument);
    }
}
