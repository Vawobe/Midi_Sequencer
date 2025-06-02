package fh.swf;

import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import lombok.Getter;

import static fh.swf.PianoGrid.CELL_HEIGHT;
import static fh.swf.PianoGrid.CELL_WIDTH;

public class NoteView extends Pane {
    private double zoom = 1.0;

    @Getter private final NoteEvent noteEvent;
    private double dragStartX;
    private double dragStartY;
    private double startWidth;
    private double startLayoutX;
    private double startLayoutY;
    private boolean resizing = false;
    private boolean dragging = false;

    public NoteView(NoteEvent noteEvent) {
        this.noteEvent = noteEvent;
        setStyle("-fx-background-color: #00AAFF; -fx-border-color: black;");
        setPrefSize(noteEvent.length*CELL_WIDTH*zoom, CELL_HEIGHT*zoom);

        initDragHandler();
    }

    private void initDragHandler() {
        setOnMouseMoved(event -> {
            if(event.getX() > getWidth() - 5) {
                setCursor(Cursor.E_RESIZE);
            } else {
                setCursor(Cursor.DEFAULT);
            }
        });

        setOnMousePressed(event -> {
            if(event.isPrimaryButtonDown()) {
                if (event.getX() > getWidth() - 5) {
                    resizing = true;
                    dragStartX = event.getSceneX();
                    startWidth = getWidth();
                } else {
                    dragging = true;
                    dragStartX = event.getSceneX();
                    dragStartY = event.getSceneY();
                    startLayoutX = getLayoutX();
                    startLayoutY = getLayoutY();
                }
                event.consume();
            } else if(event.isSecondaryButtonDown()) {
                PianoGrid parent = (PianoGrid) getParent();
                parent.getChildren().remove(this);
                parent.getNoteEvents().remove(noteEvent);
                parent.updateNotes();
            }
        });

        setOnMouseDragged(event -> {
            double zoomedCellWidth = CELL_WIDTH * zoom;
            double zoomedCellHeight = CELL_HEIGHT * zoom;

            if (resizing) {
                double deltaX = event.getSceneX() - dragStartX;
                double newWidth = Math.max(zoomedCellWidth, startWidth + deltaX);

                int beats = (int) Math.round(newWidth / zoomedCellWidth);
                newWidth = beats * zoomedCellWidth;

                setPrefWidth(newWidth);
                noteEvent.length = beats;
                event.consume();
            } else if (dragging) {
                double deltaX = event.getSceneX() - dragStartX;
                double deltaY = event.getSceneY() - dragStartY;

                int deltaCellsX = (int) Math.round(deltaX / zoomedCellWidth);
                int deltaCellsY = (int) Math.round(deltaY / zoomedCellHeight);

                double newLayoutX = Math.max(0, startLayoutX + deltaCellsX * zoomedCellWidth);
                double newLayoutY = Math.max(0, startLayoutY + deltaCellsY * zoomedCellHeight);

                setLayoutX(newLayoutX);
                setLayoutY(newLayoutY);
                noteEvent.column = (int) (newLayoutX / zoomedCellWidth);
                noteEvent.row = (int) (newLayoutY / zoomedCellHeight);
                noteEvent.midiNote = 107 - noteEvent.row;

                event.consume();
            }
        });


        setOnMouseReleased(event -> {
            resizing = false;
            dragging = false;
            dragStartX = 0;
            dragStartY = 0;
            startWidth = 0;
            startLayoutX = 0;
            startLayoutY = 0;


            PianoGrid parent = (PianoGrid) getParent();
            if(parent != null) parent.updateNotes();
            event.consume();
        });
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        updateNoteSize();
    }

    private void updateNoteSize() {
        setLayoutX(noteEvent.column * CELL_WIDTH * zoom);
        setLayoutY(noteEvent.row * CELL_HEIGHT * zoom);
        setPrefWidth(CELL_WIDTH * noteEvent.length * zoom);
        setPrefHeight(CELL_HEIGHT * zoom);
    }
}
