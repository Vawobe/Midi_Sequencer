package fh.swf;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class NoteEvent {
    private double column;
    private int row;
    private int midiNote;
    private double length;
    private int channel;

    public NoteEvent(double column, int row, double length, int channel) {
        this.column = column;
        this.row = row;
        this.midiNote = 107 - row;
        this.length = length;
        this.channel = channel;
    }
}
