package fh.swf;

import javafx.scene.Cursor;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.ArrayList;

import static fh.swf.Main.mainPane;
import static fh.swf.PianoGrid.CELL_HEIGHT;
import static fh.swf.PianoGrid.CELL_WIDTH;

public class NoteView extends Pane {
    private double zoom = 1.0;

    private ArrayList<Color> colors;

    @Getter private final NoteEvent noteEvent;
    private double dragStartX;
    private double dragStartY;
    private double startWidth;
    private double startLayoutX;
    private double startLayoutY;
    private boolean resizing = false;
    private boolean dragging = false;

    public NoteView(NoteEvent noteEvent) {
        initColors();
        this.noteEvent = noteEvent;
        setBackground(new Background(new BackgroundFill(colors.get(noteEvent.getChannel()), new CornerRadii(10), null)));
        double width = noteEvent.getLength() * CELL_WIDTH;
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
                PianoGrid parent = (PianoGrid) getParent();
                parent.getChildren().remove(this);
                parent.getNoteEvents().remove(noteEvent);
                if(parent.getNoteEvents().stream().noneMatch(note -> note.getChannel() == noteEvent.getChannel())) {
                    mainPane.getMenuBar().getInstrumentSelector().removeChannel(noteEvent.getChannel());
                }
                parent.setCurrentBeat(parent.getCurrentBeatFromPlayhead());
                parent.updateNotes();
            }
        });

        setOnMouseDragged(event -> {
            double zoomedCellWidth = CELL_WIDTH * zoom;
            double zoomedCellHeight = CELL_HEIGHT * zoom;

            double gridWidth = 4.0/mainPane.getPianoPane().getPianoGrid().getGridProperty().get();

            if (resizing) {
                double deltaX = event.getSceneX() - dragStartX;
                double newWidth = Math.max(zoomedCellWidth*gridWidth, startWidth + deltaX);
                double snappedCells = Math.round(newWidth / (zoomedCellWidth*gridWidth));
                double beats = snappedCells * gridWidth;
                newWidth = beats * zoomedCellWidth;

                setPrefWidth(newWidth);
                noteEvent.setLength(beats);
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
                noteEvent.setColumn(newColumn);
                noteEvent.setRow((int) (newLayoutY / zoomedCellHeight));
                noteEvent.setMidiNote(107 - noteEvent.getRow());

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

            mainPane.getPianoPane().getPianoGrid().setCurrentBeat(mainPane.getPianoPane().getPianoGrid().getCurrentBeatFromPlayhead());
            mainPane.getPianoPane().getPianoGrid().updateNotes();
            event.consume();
        });
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        updateNoteSize();
    }

    private void updateNoteSize() {
        setLayoutX(noteEvent.getColumn() * CELL_WIDTH * zoom);
        setLayoutY(noteEvent.getRow()* CELL_HEIGHT * zoom);
        setPrefWidth(CELL_WIDTH * noteEvent.getLength() * zoom);
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
