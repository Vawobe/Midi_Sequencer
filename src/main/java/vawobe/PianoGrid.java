package vawobe;

import vawobe.render.GridRenderer;
import vawobe.render.NoteRenderer;
import javafx.scene.layout.*;
import lombok.Getter;

public class PianoGrid extends Pane {
    @Getter private static final Playhead playhead = new Playhead();
    @Getter private static final SelectionRectangle selectionRectangle = new SelectionRectangle();

    public PianoGrid() {
        prefHeightProperty().bind(GridRenderer.getInstance().prefHeightProperty());
        prefWidthProperty().bind(GridRenderer.getInstance().prefWidthProperty());
        NoteRenderer.getInstance().prefHeightProperty().bind(GridRenderer.getInstance().prefHeightProperty());
        NoteRenderer.getInstance().prefWidthProperty().bind(GridRenderer.getInstance().prefWidthProperty());

        playhead.endYProperty().bind(heightProperty());
        playhead.setVisible(false);

        getChildren().addAll(GridRenderer.getInstance(), NoteRenderer.getInstance(), selectionRectangle, playhead);
    }
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        GridRenderer.getInstance().drawGrid();
    }
}
