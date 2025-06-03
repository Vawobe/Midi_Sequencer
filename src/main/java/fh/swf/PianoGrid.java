package fh.swf;

import fh.swf.controller.PlaybackController;
import fh.swf.render.GridRenderer;
import fh.swf.render.NoteRenderer;
import javafx.scene.layout.*;
import lombok.Getter;

import static fh.swf.render.GridRenderer.CELL_WIDTH;

public class PianoGrid extends Pane {
    @Getter private static final Playhead playhead = new Playhead();

    public PianoGrid() {
        setFocusTraversable(false);
        prefHeightProperty().bind(GridRenderer.getInstance().prefHeightProperty());
        prefWidthProperty().bind(GridRenderer.getInstance().prefWidthProperty());
        NoteRenderer.getInstance().prefHeightProperty().bind(GridRenderer.getInstance().prefHeightProperty());
        NoteRenderer.getInstance().prefWidthProperty().bind(GridRenderer.getInstance().prefWidthProperty());

        playhead.endYProperty().bind(heightProperty());
        playhead.setVisible(false);
        getChildren().addAll(GridRenderer.getInstance(), NoteRenderer.getInstance(), playhead);
    }
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        GridRenderer.getInstance().drawGrid();
    }

    public void setZoom(double zoom) {
        GridRenderer.zoom = zoom;
        GridRenderer.getInstance().updateGridSize();
        playhead.setStartX(PlaybackController.getInstance().getCurrentBeat()*CELL_WIDTH*GridRenderer.zoom);
    }
}
