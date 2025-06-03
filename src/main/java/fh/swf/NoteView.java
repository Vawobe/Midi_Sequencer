package fh.swf;

import fh.swf.controller.PlaybackController;
import fh.swf.model.manager.NoteManager;
import fh.swf.render.GridRenderer;
import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.ArrayList;

import static fh.swf.Main.mainPane;
import static fh.swf.render.GridRenderer.CELL_HEIGHT;
import static fh.swf.render.GridRenderer.CELL_WIDTH;

public class NoteView extends Pane {
    private double zoom = 1.0;

    private ArrayList<Color> colors;

    @Getter private final Note note;
    private double dragStartX;
    private double dragStartY;
    private double startWidth;
    private double startLayoutX;
    private double startLayoutY;
    private boolean resizing = false;
    private boolean dragging = false;

    public NoteView(Note note) {
        initColors();
        this.note = note;
        setBackground(new Background(new BackgroundFill(colors.get(note.getChannel()), new CornerRadii(10), null)));
        double width = note.getLength() * CELL_WIDTH;
        setPrefSize(width*zoom, CELL_HEIGHT*zoom);

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
                NoteManager.getInstance().removeNote(this.getNote());
                if(NoteManager.getInstance().getNotes().stream().noneMatch(note -> note.getChannel() == this.note.getChannel())) {
                    mainPane.getMenuBar().getInstrumentSelector().removeChannel(note.getChannel());
                }
                PlaybackController.getInstance().setCurrentBeat(PianoGrid.getPlayhead().getCurrentBeat());
                PlaybackController.getInstance().updateNotes();
            }
        });

        setOnMouseDragged(event -> {
            double zoomedCellWidth = CELL_WIDTH * zoom;
            double zoomedCellHeight = CELL_HEIGHT * zoom;

            double gridWidth = 4.0/ GridRenderer.getInstance().getGridProperty().get();

            if (resizing) {
                double deltaX = event.getSceneX() - dragStartX;
                double newWidth = Math.max(zoomedCellWidth*gridWidth, startWidth + deltaX);
                double snappedCells = Math.round(newWidth / (zoomedCellWidth*gridWidth));
                double beats = snappedCells * gridWidth;
                newWidth = beats * zoomedCellWidth;

                setPrefWidth(newWidth);
                note.setLength(beats);
                event.consume();
            } else if (dragging) {
                double deltaX = event.getSceneX() - dragStartX;
                double deltaY = event.getSceneY() - dragStartY;

                int deltaCellsX = (int) Math.round(deltaX / (zoomedCellWidth*gridWidth));
                int deltaCellsY = (int) Math.round(deltaY / zoomedCellHeight);

                double newLayoutX = Math.max(0, startLayoutX + deltaCellsX * (zoomedCellWidth*gridWidth));
                double newLayoutY = Math.max(0, startLayoutY + deltaCellsY * zoomedCellHeight);

                setLayoutX(newLayoutX);
                setLayoutY(newLayoutY);


                double newColumn = (newLayoutX/zoomedCellWidth);
                note.setColumn(newColumn);
                note.setRow((int) (newLayoutY / zoomedCellHeight));
                note.setMidiNote(107 - note.getRow());

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

            PlaybackController.getInstance().setCurrentBeat(PianoGrid.getPlayhead().getCurrentBeat());
            PlaybackController.getInstance().updateNotes();
            event.consume();
        });
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        updateNoteSize();
    }

    private void updateNoteSize() {
        setLayoutX(note.getColumn() * CELL_WIDTH * zoom);
        setLayoutY(note.getRow()* CELL_HEIGHT * zoom);
        setPrefWidth(CELL_WIDTH * note.getLength() * zoom);
        setPrefHeight(CELL_HEIGHT * zoom);
    }

    private void initColors() {
        colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        colors.add(Color.GREEN);
        colors.add(Color.VIOLET);
        colors.add(Color.BLACK);
        colors.add(Color.BEIGE);

    }
}
