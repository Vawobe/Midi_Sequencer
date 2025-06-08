package vawobe.save;

import lombok.Getter;
import vawobe.Note;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
public class ProjectData implements Serializable {
    @Serial private static final long serialVersionUID = 1L;

    public int bpm;
    public int signature;
    public List<Note> notes;

    public ProjectData(int bpm, int signature, List<Note> notes) {
        this.bpm = bpm;
        this.notes = notes;
        this.signature = signature;
    }
}
