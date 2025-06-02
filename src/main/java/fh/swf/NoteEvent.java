package fh.swf;

public class NoteEvent {
    public int column;
    public int row;
    public int midiNote;
    public double length;

    public NoteEvent(int column, int row, double length) {
        this.column = column;
        this.row = row;
        this.midiNote = 107 - row;
        this.length = length;
    }
}
