package vawobe;

import vawobe.controller.PlaybackController;
import vawobe.enums.Instruments;
import vawobe.model.manager.NoteManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;

import static vawobe.Main.mainPane;

@Getter
public class NoteViewModel {
    private final Note note;

    private final SimpleObjectProperty<Instruments> instrumentProperty;
    private final SimpleIntegerProperty rowProperty;
    private final SimpleDoubleProperty columnProperty;
    private final SimpleIntegerProperty midiNoteProperty;
    private final SimpleDoubleProperty lengthProperty;
    private final SimpleIntegerProperty channelProperty;
    private final SimpleIntegerProperty velocityProperty;

    public NoteViewModel(Note note) {
        this.note = note;
        instrumentProperty = new SimpleObjectProperty<>(note.getInstrument());
        rowProperty = new SimpleIntegerProperty(note.getRow());
        columnProperty = new SimpleDoubleProperty(note.getColumn());
        midiNoteProperty = new SimpleIntegerProperty(note.getMidiNote());
        lengthProperty = new SimpleDoubleProperty(note.getLength());
        channelProperty = new SimpleIntegerProperty(note.getChannel());
        velocityProperty = new SimpleIntegerProperty(note.getVelocity());
    }

    public void updateNote() {
        note.setInstrument(instrumentProperty.get());
        note.setRow(rowProperty.get());
        note.setColumn(columnProperty.get());
        note.setMidiNote(midiNoteProperty.get());
        note.setLength(lengthProperty.get());
        note.setChannel(channelProperty.get());
        note.setVelocity(velocityProperty.get());
    }

    public void calculateMidiNote() {
        midiNoteProperty.set(107 - getRowProperty().get());
    }

    public void deleteNote() {
        NoteManager.getInstance().removeNote(note);
        if(NoteManager.getInstance().getNotes().stream().noneMatch(note -> note.getChannel() == this.note.getChannel())) {
            mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().removeChannel(note.getChannel());
        }
        PlaybackController.getInstance().updateNotes();
    }
}
