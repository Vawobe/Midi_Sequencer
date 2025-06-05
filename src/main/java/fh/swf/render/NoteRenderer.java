package fh.swf.render;

import fh.swf.Note;
import fh.swf.NoteView;
import fh.swf.PianoGrid;
import fh.swf.controller.PlaybackController;
import fh.swf.enums.Modes;
import fh.swf.model.manager.MidiManager;
import fh.swf.model.manager.ModeManager;
import fh.swf.model.manager.NoteManager;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;

import static fh.swf.Main.mainPane;
import static fh.swf.render.GridRenderer.CELL_HEIGHT;
import static fh.swf.render.GridRenderer.CELL_WIDTH;

public class NoteRenderer extends Pane {
    private static NoteRenderer instance;
    private boolean isDragging;

    public static NoteRenderer getInstance() {
        if(instance == null) {
            instance = new NoteRenderer();
        }
        return instance;
    }

    private NoteRenderer() {
        isDragging = false;
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

        setOnMouseClicked(this::onMouseClickedEvent);
        setOnMouseDragged(this::onMouseDraggedEvent);
        setOnMouseReleased(this::onMouseReleasedEvent);
    }

    private void onMouseDraggedEvent(MouseEvent event) {
        isDragging = true;
    }
    private void onMouseReleasedEvent(MouseEvent event) {
        isDragging = false;
    }

    private void onMouseClickedEvent(MouseEvent event) {
        if(ModeManager.getInstance().getCurrentModeProperty().get() == Modes.DRAW) {
            if (event.getTarget() == this && !isDragging) {
                double length = 4.0 / GridRenderer.getInstance().getGridProperty().get();

                int cell = (int) Math.ceil(event.getX() / (length * GridRenderer.zoom * 100)) - 1;
                double col = cell * length;
                int row = (int) (event.getY() / (CELL_HEIGHT * GridRenderer.zoom));

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
                PlaybackController.getInstance().setCurrentBeat(PianoGrid.getPlayhead().getCurrentBeat());
                PlaybackController.getInstance().updateNotes();
            }
        }
    }
}
