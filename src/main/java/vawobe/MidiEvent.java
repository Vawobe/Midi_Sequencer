package vawobe;

public class MidiEvent {
    public final double time;
    public final Note note;
    public final boolean isOn;

    public MidiEvent(double time, Note note, boolean isOn) {
        this.time = time;
        this.note = note;
        this.isOn = isOn;
    }
}
