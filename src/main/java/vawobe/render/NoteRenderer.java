package vawobe.render;

import javafx.collections.SetChangeListener;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import vawobe.*;
import vawobe.commands.AddNotesCommand;
import vawobe.commands.RemoveNotesCommand;
import vawobe.commands.SelectNotesCommand;
import vawobe.controller.ClipboardController;
import vawobe.controller.PlaybackController;
import vawobe.enums.Instruments;
import vawobe.enums.Modes;
import vawobe.model.manager.*;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.clamp;
import static vawobe.Main.mainPane;
import static vawobe.render.GridRenderer.CELL_HEIGHT;
import static vawobe.render.GridRenderer.CELL_WIDTH;

public class NoteRenderer extends Pane {
    private static NoteRenderer instance;

    public static NoteRenderer getInstance() {
        if(instance == null) {
            instance = new NoteRenderer();
        }
        return instance;
    }

    private NoteRenderer() {
        SelectionManager.getInstance().getSelectedNotes().addListener((SetChangeListener<NoteView>) change -> {
            if(change.wasRemoved()) change.getElementRemoved().getSelectedProperty().set(false);
            if(change.wasAdded()) change.getElementAdded().getSelectedProperty().set(true);
        });

        NoteManager.getInstance().getNotesList().addListener((ListChangeListener<Note>) change -> {
            while (change.next()) {
                if(change.wasRemoved()) {
                    for(Note note : change.getRemoved()) {
                        ArrayList<NoteView> noteViewsToRemove = new ArrayList<>();
                        getChildren().forEach(node -> {
                            if(node instanceof NoteView noteView) {
                                if(noteView.getViewModel().getNote() == note) {
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
        else if (ModeManager.getInstance().getCurrentModeProperty().get() == Modes.DRAG_TO_SCROLL) {
            Point2D lastPoint = (Point2D) getUserData();
            if (lastPoint != null) {
                double dx = event.getSceneX() - lastPoint.getX();
                double dy = event.getSceneY() - lastPoint.getY();


                scrollBy(dx, dy);
                setUserData(new Point2D(event.getSceneX(), event.getSceneY()));
            }
        }
        
    }
    private void onMouseReleasedEvent(MouseEvent event) {
        if(ModeManager.getInstance().getCurrentModeProperty().get() == Modes.SELECT) {
            SelectionRectangle selectionRectangle = PianoGrid.getSelectionRectangle();
            Set<NoteView> oldSelect = new HashSet<>(SelectionManager.getInstance().getSelectedNotes());
            Set<NoteView> newSelect = new HashSet<>(SelectionManager.getInstance().getSelectedNotes());
            newSelect.addAll(selectionRectangle.getAllNotesInRectangle());
            CommandManager.getInstance().executeCommand(new SelectNotesCommand(oldSelect, newSelect));
            selectionRectangle.setVisible(false);
        }
        
    }

    private void onMousePressed(MouseEvent event) {
        if(!event.isShiftDown()) {
            Set<NoteView> oldSelection = new HashSet<>(SelectionManager.getInstance().getSelectedNotes());
            CommandManager.getInstance().executeCommand(new SelectNotesCommand(oldSelection, new HashSet<>()));
        }
        switch (ModeManager.getInstance().getCurrentModeProperty().get()) {
            case DRAW -> { if (event.getTarget() == this) addNote(event.getX(), event.getY()); }
            case SELECT -> {
                SelectionRectangle selectionRectangle = PianoGrid.getSelectionRectangle();
                selectionRectangle.setVisible(true);
                selectionRectangle.setClickedX(event.getX());
                selectionRectangle.setClickedY(event.getY());
            }
            case DRAG_TO_SCROLL -> setUserData(new Point2D(event.getSceneX(), event.getSceneY()));
        }
        
    }

    public void onKeyPressedEvent(KeyEvent event) {
        if (event.isControlDown()) {
            switch (event.getCode()) {
                case A -> selectAll();
                case C -> ClipboardController.getInstance().copySelectedNotes();
                case X -> ClipboardController.getInstance().cutSelectedNotes();
                case V -> {
                    boolean altPressed = event.isAltDown();
                    ClipboardController.getInstance().pasteNotes(!altPressed);
                }
                case Z -> CommandManager.getInstance().undo();
                case Y -> CommandManager.getInstance().redo();
            }
            
        } else {
            switch (event.getCode()) {
                case DELETE:
                case BACK_SPACE:
                    ArrayList<NoteView> notesToDelete = new ArrayList<>();
                    getChildren().forEach(node -> {
                        if (node instanceof NoteView noteView) {
                            if(noteView.getSelectedProperty().get()) {
                                notesToDelete.add(noteView);
                            }
                        }
                    });
                    CommandManager.getInstance().executeCommand(new RemoveNotesCommand(notesToDelete));
                    break;
                case SPACE:
                    if(PlaybackController.getInstance().isPlaying()) PlaybackController.getInstance().pausePlayback();
                    else PlaybackController.getInstance().startPlayback();
                    break;
                case ESCAPE:
                    PlaybackController.getInstance().stopPlayback();
                    break;
                case UP: scrollBy(0, 10); break;
                case DOWN: scrollBy(0, -10); break;
                case LEFT: scrollBy(20, 0);  break;
                case RIGHT: scrollBy(-20, 0);  break;
                case TAB:
                    mainPane.getMenuBar().getModeButtonBox().toggleNextButton();
                    break;
            }
            
        }
    }

    public void addNote(double x, double y) {
        int channel = mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().getCurrentInstrumentsChannel();
        if(channel == -1) channel = mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().addCurrentInstrument();

        if(channel != -1) {
            double length = 4.0 / GridRenderer.getInstance().getGridProperty().get();

            int cell = (int) Math.ceil(x / (length * PianoGridPane.zoomX.get() * 100)) - 1;
            double col = cell * length;
            int row = (int) (y / (CELL_HEIGHT * PianoGridPane.zoomY.get()));

            Note note = new Note(col, row, length, channel, mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().getValue());

            if (!PlaybackController.getInstance().isPlaying()) {
                MidiManager.getInstance().playNote(note);

                PauseTransition pause = new PauseTransition(Duration.millis(200));
                pause.setOnFinished(_ -> MidiManager.getInstance().stopNote(note));
                pause.play();
            }
            NoteView noteView = new NoteView(note);

            double snappedX = note.getColumn() * CELL_WIDTH * (4.0/GridRenderer.getInstance().getGridProperty().get()) * PianoGridPane.zoomX.get();
            double snappedY = note.getRow() * CELL_HEIGHT * PianoGridPane.zoomY.get();

            noteView.setLayoutX(snappedX);
            noteView.setLayoutY(snappedY);
            noteView.updateNoteSize();
            ArrayList<NoteView> noteViews = new ArrayList<>();
            noteViews.add(noteView);

            CommandManager.getInstance().executeCommand(new AddNotesCommand(noteViews));
            SelectionManager.getInstance().getSelectedNotes().add(noteView);

            PlaybackController.getInstance().updateNotes();
        } else {
            System.err.println("Keine freien Kanäle");
        }
    }

    public void selectAllNotesWithInstrument(Instruments instrument) {
        Set<NoteView> oldSelection = new HashSet<>(SelectionManager.getInstance().getSelectedNotes());
        Set<NoteView> newSelection = new HashSet<>();
        for(Node node : getChildren()) {
            if(node instanceof NoteView noteView) {
                if(noteView.getViewModel().getInstrumentProperty().get() == instrument) {
                    newSelection.add(noteView);
                }
            }
        }
        CommandManager.getInstance().executeCommand(new SelectNotesCommand(oldSelection, newSelection));
    }

    public void selectAll() {
        Set<NoteView> oldSelection = new HashSet<>(SelectionManager.getInstance().getSelectedNotes());
        Set<NoteView> newSelection = new HashSet<>();
        for(Node node : getChildren()) {
            if(node instanceof NoteView noteView) {
                newSelection.add(noteView);
            }
        }
        CommandManager.getInstance().executeCommand(new SelectNotesCommand(oldSelection, newSelection));
    }

    public void addNoteView(NoteView noteView) {
        getChildren().add(noteView);
        NoteManager.getInstance().addNote(noteView.getViewModel().getNote());
    }

    public void removeNoteView(NoteView noteView) {
        getChildren().remove(noteView);
        NoteManager.getInstance().removeNote(noteView.getViewModel().getNote());
    }

    private void scrollBy(double dx, double dy) {
        BasicScrollPane scroll = mainPane.getPianoGridPane().getPianoGridScrollPane();

        double vScrollSpeed = 0.0005;
        double hScrollSpeed = 0.01;

        double h = scroll.getHvalue() - dx * hScrollSpeed;
        double v = scroll.getVvalue() - dy * vScrollSpeed;

        scroll.setHvalue(clamp(h, 0, 1));
        scroll.setVvalue(clamp(v, 0, 1));
    }
}
