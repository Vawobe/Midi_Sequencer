package vawobe.save;

import vawobe.Note;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public record ProjectData(int bpm, int signature, List<Note> notes) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

}
