package fh.swf;

import fh.swf.controller.PlaybackController;
import fh.swf.enums.Modes;
import fh.swf.model.manager.MidiManager;
import fh.swf.model.manager.ModeManager;
import fh.swf.render.GridRenderer;
import fh.swf.render.NoteRenderer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

import static fh.swf.render.GridRenderer.CELL_HEIGHT;
import static fh.swf.render.GridRenderer.CELL_WIDTH;

public class NoteView extends Pane {
    @Getter private final NoteViewModel viewModel;
    private double dragStartX;
    private double dragStartY;
    private double startWidth;
    private double startLayoutX;
    private double startLayoutY;
    private boolean resizing = false;
    private boolean dragging = false;
    @Getter private final SimpleBooleanProperty selectedProperty = new SimpleBooleanProperty(false);

    public NoteView(Note note) {
        this.viewModel = new NoteViewModel(note);
        setBackground(new Background(new BackgroundFill(note.getInstrument().getColor(), new CornerRadii(10), null)));
        setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(10), null)));
        double width = note.getLength() * CELL_WIDTH;
        setPrefSize(width*PianoGridPane.zoomX, CELL_HEIGHT*PianoGridPane.zoomY);

        selectedProperty.addListener((_,_,newValue) ->
                setBorder(new Border(new BorderStroke(Color.DARKGRAY, newValue ? BorderStrokeStyle.DASHED :
                        BorderStrokeStyle.SOLID, new CornerRadii(10), null))));

        viewModel.getInstrumentProperty().addListener((_,_,newValue) ->
                setBackground(new Background(new BackgroundFill(newValue.getColor(), new CornerRadii(10), null))));

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
                if(!selectedProperty.get()) {
                    if(!event.isShiftDown()) {
                        NoteRenderer.getInstance().getChildren().forEach(node -> {
                            if (node instanceof NoteView noteView) noteView.getSelectedProperty().set(false);
                        });
                    }
                    selectedProperty.set(true);
                } else {
                    if(event.isShiftDown()) selectedProperty.set(false);
                }
                if (event.getX() > getWidth() - 5) {
                    resizing = true;
                    dragStartX = event.getSceneX();
                    startWidth = getWidth();
                } else {
                    dragStartX = event.getSceneX();
                    dragStartY = event.getSceneY();
                    startLayoutX = getLayoutX();
                    startLayoutY = getLayoutY();
                }
                event.consume();
            } else if(event.isSecondaryButtonDown()) {
                viewModel.deleteNote();
            }
        });

        setOnMouseDragged(event -> {
            if(!resizing) dragging = true;

            double zoomedCellWidth = CELL_WIDTH * PianoGridPane.zoomX;
            double zoomedCellHeight = CELL_HEIGHT * PianoGridPane.zoomY;

            double gridWidth = 4.0/ GridRenderer.getInstance().getGridProperty().get();

            if (resizing) {
                double deltaX = event.getSceneX() - dragStartX;
                double newWidth = Math.max(zoomedCellWidth*gridWidth, startWidth + deltaX);
                double snappedCells = Math.round(newWidth / (zoomedCellWidth*gridWidth));
                double beats = snappedCells * gridWidth;
                newWidth = beats * zoomedCellWidth;

                setPrefWidth(newWidth);
                viewModel.getLengthProperty().set(beats);
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
                viewModel.getColumnProperty().set(newColumn);
                viewModel.getRowProperty().set((int) (newLayoutY / zoomedCellHeight));
                viewModel.calculateMidiNote();

                event.consume();
            }
        });


        setOnMouseReleased(event -> {
            MidiManager.getInstance().stopNote(viewModel.getNote());
            viewModel.updateNote();
            if(!dragging) {
                if(ModeManager.getInstance().getCurrentModeProperty().get() == Modes.ERASE) {
                    viewModel.deleteNote();
                }
            }
            resetAttributes();

            PlaybackController.getInstance().updateNotes();
            event.consume();
        });
    }

    public void updateNoteSize() {
        setLayoutX(viewModel.getColumnProperty().get() * CELL_WIDTH * PianoGridPane.zoomX);
        setLayoutY(viewModel.getRowProperty().get() * CELL_HEIGHT * PianoGridPane.zoomY);
        setPrefWidth(CELL_WIDTH * viewModel.getLengthProperty().get() * PianoGridPane.zoomX);
        setPrefHeight(CELL_HEIGHT * PianoGridPane.zoomY);
    }

    private void resetAttributes() {
        resizing = false;
        dragging = false;
        dragStartX = 0;
        dragStartY = 0;
        startWidth = 0;
        startLayoutX = 0;
        startLayoutY = 0;
    }
}
