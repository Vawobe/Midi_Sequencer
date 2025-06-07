package fh.swf;

import fh.swf.enums.Instruments;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class Note {
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
        this.midiNote = 107 - row;
        this.length = length;
        this.channel = channel;
        this.velocity = 100;
        this.instrument = instrument;
    }
}
