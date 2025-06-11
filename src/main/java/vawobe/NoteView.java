package vawobe;

import lombok.Setter;
import vawobe.controller.PlaybackController;
import vawobe.enums.Modes;
import vawobe.model.manager.MidiManager;
import vawobe.model.manager.ModeManager;
import vawobe.render.GridRenderer;
import vawobe.render.NoteRenderer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.List;

import static vawobe.Main.mainPane;
import static vawobe.render.GridRenderer.CELL_HEIGHT;
import static vawobe.render.GridRenderer.CELL_WIDTH;

public class NoteView extends Pane {
    @Getter private final NoteViewModel viewModel;
    @Getter @Setter private double dragStartX;
    @Getter @Setter private double dragStartY;
    private double startWidth;
    @Getter @Setter private double startLayoutX;
    @Getter @Setter private double startLayoutY;
    private boolean resizing = false;
    private boolean dragging = false;
    @Getter private final SimpleBooleanProperty selectedProperty = new SimpleBooleanProperty(false);

    public NoteView(Note note) {
        this.viewModel = new NoteViewModel(note);
        setBackground(new Background(new BackgroundFill(note.getInstrument().getColor(), new CornerRadii(10), null)));
        setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, new CornerRadii(10), null)));
        double width = note.getLength() * CELL_WIDTH;
        setPrefSize(width*PianoGridPane.zoomX.get(), CELL_HEIGHT*PianoGridPane.zoomY.get());

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
                if(event.isControlDown()) {
                    mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().setValue(getViewModel().getInstrumentProperty().getValue());
                } else if(event.isAltDown() && event.isShiftDown()){
                    NoteRenderer.getInstance().selectAllNotesWithInstrument(viewModel.getInstrumentProperty().get());
                } else {
                    List<NoteView> selected = NoteRenderer.getInstance().getSelectedNotes();
                    if(!selected.contains(this)) {
                        if(!event.isShiftDown()) selected.clear();
                        selected.add(this);
                    } else if(event.isShiftDown()) selected.remove(this);

                    if (event.getX() > getWidth() - 5) {
                        resizing = true;
                        dragStartX = event.getSceneX();
                        startWidth = getWidth();
                    } else {
                        if (!NoteRenderer.getInstance().getSelectedNotes().isEmpty()) {
                            dragStartX = event.getSceneX();
                            dragStartY = event.getSceneY();
                            for (NoteView noteView : NoteRenderer.getInstance().getSelectedNotes()) {
                                noteView.setStartLayoutX(noteView.getLayoutX());
                                noteView.setStartLayoutY(noteView.getLayoutY());
                            }
                        }
                    }
                }
                event.consume();
            } else if(event.isSecondaryButtonDown()) {
                viewModel.deleteNote();
            }
        });

        setOnMouseDragged(event -> {
            if(!resizing) dragging = true;

            double zoomedCellWidth = CELL_WIDTH * PianoGridPane.zoomX.get();
            double zoomedCellHeight = CELL_HEIGHT * PianoGridPane.zoomY.get();

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
                if(!NoteRenderer.getInstance().getSelectedNotes().isEmpty()) {
                    for(NoteView noteView : NoteRenderer.getInstance().getSelectedNotes()) {
                        double deltaX = event.getSceneX() - dragStartX;
                        double deltaY = event.getSceneY() - dragStartY;

                        int deltaCellsX = (int) Math.round(deltaX / (zoomedCellWidth*gridWidth));
                        int deltaCellsY = (int) Math.round(deltaY / zoomedCellHeight);

                        double newLayoutX = Math.max(0, noteView.getStartLayoutX() + deltaCellsX * (zoomedCellWidth*gridWidth));
                        double newLayoutY = Math.max(0, noteView.getStartLayoutY() + deltaCellsY * zoomedCellHeight);

                        noteView.setLayoutX(newLayoutX);
                        noteView.setLayoutY(newLayoutY);


                        double newColumn = (newLayoutX/zoomedCellWidth);
                        noteView.getViewModel().getColumnProperty().set(newColumn);
                        noteView.getViewModel().getRowProperty().set((int) (newLayoutY / zoomedCellHeight));
                        noteView.getViewModel().calculateMidiNote();
                    }
                }

                event.consume();
            }
        });


        setOnMouseReleased(event -> {
            if(!NoteRenderer.getInstance().getSelectedNotes().isEmpty()) {
                for(NoteView noteView : NoteRenderer.getInstance().getSelectedNotes()) {
                    MidiManager.getInstance().stopNote(noteView.getViewModel().getNote());
                    noteView.getViewModel().updateNote();
                    if(!dragging) {
                        if(ModeManager.getInstance().getCurrentModeProperty().get() == Modes.ERASE) {
                            noteView.getViewModel().deleteNote();
                        }
                    }
                    noteView.resetAttributes();
                }
            }

            PlaybackController.getInstance().updateNotes();
            event.consume();
        });
    }

    public void updateNoteSize() {
        setLayoutX(viewModel.getColumnProperty().get() * CELL_WIDTH * PianoGridPane.zoomX.get());
        setLayoutY(viewModel.getRowProperty().get() * CELL_HEIGHT * PianoGridPane.zoomY.get());
        setPrefWidth(CELL_WIDTH * viewModel.getLengthProperty().get() * PianoGridPane.zoomX.get());
        setPrefHeight(CELL_HEIGHT * PianoGridPane.zoomY.get());
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
