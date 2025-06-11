package vawobe;

import vawobe.enums.Instruments;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter @Getter
public class Note implements Serializable {
    private double column;
    private int row;
    private int midiNote;
    private double length;
    private int channel;
    private int velocity;
    private Instruments instrument;

    public Note(double column, int row, double length, int channel, Instruments instrument) {
        this.column = column;
        this.row = row;
        this.midiNote = 107 - row - 12;
        this.length = length;
        this.channel = channel;
        this.velocity = 100;
        this.instrument = instrument;
    }

    public Note(Note original) {
        this.column = original.getColumn();
        this.row = original.getRow();
        this.midiNote = original.getMidiNote();
        this.length = original.getLength();
        this.channel = original.getChannel();
        this.velocity = original.getVelocity();
        this.instrument = original.getInstrument();
    }
}
