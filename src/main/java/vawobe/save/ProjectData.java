package vawobe.save;

import vawobe.Note;

import java.io.Serializable;
import java.util.List;

public class ProjectData implements Serializable{
    private static final long serialVersionUID = 1L;
    public final int bpm;
    public final int signature;
    public final List<Note> notes;

    public ProjectData(int bpm, int signature, List<Note> notes)  {
        this.bpm = bpm;
        this.signature = signature;
        this.notes = notes;
    }
}
