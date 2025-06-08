package vawobe.save;

import vawobe.Note;
import vawobe.enums.Instruments;

import java.io.Serial;
import java.io.Serializable;

public class NoteData implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    public double column;
    public int row;
    public int midiNote;
    public double length;
    public int channel;
    public int velocity;
    public Instruments instrument;

    public NoteData(Note note) {
        this.column = note.getColumn();
        this.row = note.getRow();
        this.midiNote = note.getMidiNote();
        this.length = note.getLength();
        this.channel = note.getChannel();
        this.velocity = note.getVelocity();
        this.instrument = note.getInstrument();
    }
}
