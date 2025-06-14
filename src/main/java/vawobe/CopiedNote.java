package vawobe;

import lombok.Getter;
import vawobe.enums.Instruments;
import vawobe.render.GridRenderer;

@Getter
public class CopiedNote {
    private final double layoutX;
    private final int row;
    private final double column;
    private final double length;
    private final int velocity;
    private final Instruments instrument;
    private final int columnsPerFullNote;

    public CopiedNote(NoteView noteView) {
        layoutX = noteView.getLayoutX();
        row = noteView.getViewModel().getRowProperty().get();
        length = noteView.getViewModel().getLengthProperty().get();
        velocity = noteView.getViewModel().getVelocityProperty().get();
        instrument = noteView.getViewModel().getInstrumentProperty().get();
        column = noteView.getViewModel().getColumnProperty().get();
        columnsPerFullNote = GridRenderer.getInstance().getGridProperty().get();
    }
}
