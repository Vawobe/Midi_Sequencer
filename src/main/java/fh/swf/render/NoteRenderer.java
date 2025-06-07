package fh.swf.render;

import fh.swf.*;
import fh.swf.controller.PlaybackController;
import fh.swf.enums.Modes;
import fh.swf.model.manager.MidiManager;
import fh.swf.model.manager.ModeManager;
import fh.swf.model.manager.NoteManager;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

import static fh.swf.Main.mainPane;
import static fh.swf.render.GridRenderer.CELL_HEIGHT;
import static fh.swf.render.GridRenderer.CELL_WIDTH;

public class NoteRenderer extends Pane {
    private static NoteRenderer instance;

    public static NoteRenderer getInstance() {
        if(instance == null) {
            instance = new NoteRenderer();
        }
        return instance;
    }

    private NoteRenderer() {
        NoteManager.getInstance().getNotes().addListener((ListChangeListener<Note>) change -> {
            while (change.next()) {
                if(change.wasAdded()) {
                    for(Note note : change.getAddedSubList()) {
                        NoteView noteView = new NoteView(note);

                        double snappedX = note.getColumn() * CELL_WIDTH * (4.0/GridRenderer.getInstance().getGridProperty().get()) * GridRenderer.zoom;
                        double snappedY = note.getRow() * CELL_HEIGHT * GridRenderer.zoom;

                        noteView.setLayoutX(snappedX);
                        noteView.setLayoutY(snappedY);
                        noteView.setZoom(GridRenderer.zoom);

                        getChildren().add(noteView);
                    }
                } else if(change.wasRemoved()) {
                    for(Note note : change.getRemoved()) {
                        ArrayList<NoteView> noteViewsToRemove = new ArrayList<>();
                        getChildren().forEach(node -> {
                            if(node instanceof NoteView noteView) {
                                if(noteView.getNote() == note) {
                                    noteViewsToRemove.add(noteView);
                                }
                            }
                        });
                        getChildren().removeAll(noteViewsToRemove);
                    }
                }
            }
        });

        setOnMousePressed(this::onMousePressed);
        setOnMouseDragged(this::onMouseDraggedEvent);
        setOnMouseReleased(this::onMouseReleasedEvent);
    }

    private void onMouseDraggedEvent(MouseEvent event) {
        if(ModeManager.getInstance().getCurrentModeProperty().get() == Modes.SELECT) {
            SelectionRectangle selectionRectangle = PianoGrid.getSelectionRectangle();
            if(selectionRectangle.isVisible()) {
                selectionRectangle.setX(Math.min(selectionRectangle.getClickedX(), event.getX()));
                selectionRectangle.setY(Math.min(selectionRectangle.getClickedY(), event.getY()));
                selectionRectangle.setWidth(Math.abs(event.getX() - selectionRectangle.getClickedX()));
                selectionRectangle.setHeight(Math.abs(event.getY() - selectionRectangle.getClickedY()));
            }
        }
        event.consume();
    }
    private void onMouseReleasedEvent(MouseEvent event) {
        if(ModeManager.getInstance().getCurrentModeProperty().get() == Modes.SELECT) {
            SelectionRectangle selectionRectangle = PianoGrid.getSelectionRectangle();
            selectionRectangle.getAllNotesInRectangle().forEach(noteView -> noteView.getSelectedProperty().set(true));
            selectionRectangle.setVisible(false);
        }
        event.consume();
    }

    private void onMousePressed(MouseEvent event) {
        if(!event.isShiftDown()) {
            getChildren().forEach(node -> {
                if (node instanceof NoteView note) note.getSelectedProperty().set(false);
            });
        }
        switch (ModeManager.getInstance().getCurrentModeProperty().get()) {
            case DRAW:
                if (event.getTarget() == this) addNote(event.getX(), event.getY());
                break;
            case SELECT:
                SelectionRectangle selectionRectangle = PianoGrid.getSelectionRectangle();
                selectionRectangle.setVisible(true);
                selectionRectangle.setClickedX(event.getX());
                selectionRectangle.setClickedY(event.getY());
                break;
        }
        event.consume();
    }

    public void onKeyPressedEvent(KeyEvent event) {
        if (event.isControlDown() && event.getCode() == KeyCode.A) {
            getChildren().forEach(node -> {
                if(node instanceof NoteView noteView) noteView.getSelectedProperty().set(true);
            });
            event.consume();
        }
    }

    private void addNote(double x, double y) {
        double length = 4.0 / GridRenderer.getInstance().getGridProperty().get();

        int cell = (int) Math.ceil(x / (length * GridRenderer.zoom * 100)) - 1;
        double col = cell * length;
        int row = (int) (y / (CELL_HEIGHT * GridRenderer.zoom));

        int channel = mainPane.getMenuBar().getInstrumentSelector().getCurrentInstrumentsChannel();

        if (channel == -1) {
            channel = mainPane.getMenuBar().getInstrumentSelector().getNextFreeChannel();
            mainPane.getMenuBar().getInstrumentSelector().addCurrentInstrument(channel);
        }

        Note note = new Note(col, row, length, channel);
        NoteManager.getInstance().addNote(note);

        if (!PlaybackController.getInstance().isPlaying()) {
            MidiManager.getInstance().playNote(note);

            PauseTransition pause = new PauseTransition(Duration.millis(200));
            pause.setOnFinished(_ -> MidiManager.getInstance().stopNote(note));
            pause.play();
        }
        PlaybackController.getInstance().updateNotes();
    }
}
