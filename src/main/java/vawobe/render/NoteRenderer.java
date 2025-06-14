package vawobe.render;

import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import vawobe.*;
import vawobe.commands.AddNotesCommand;
import vawobe.commands.MoveNotesCommand;
import vawobe.commands.SelectNotesCommand;
import vawobe.manager.*;
import vawobe.enums.Instruments;
import javafx.animation.PauseTransition;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.util.*;

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

        setOnMousePressed(EventManager::onMousePressed);
        setOnMouseDragged(EventManager::onMouseDraggedEvent);
        setOnMouseReleased(EventManager::onMouseReleasedEvent);
    }

    public void addNote(double x, double y) {
        int channel = mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().getCurrentInstrumentsChannel();
        if(channel == -1) channel = mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().addCurrentInstrument();

        if(channel != -1) {
            double snappedX = GridRenderer.getInstance().snapXLayout(x);
            double snappedY = GridRenderer.getInstance().snapYToGrid(y);
            double column = GridRenderer.getInstance().xAsColumn(snappedX);
            int row = GridRenderer.getInstance().yAsRow(snappedY);
            double length = 4.0 / GridRenderer.getInstance().getGridProperty().get();

            Note note = new Note(column, row, length, channel, mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().getValue());

            if (!PlaybackManager.getInstance().isPlaying()) {
                PauseTransition pause = new PauseTransition(Duration.millis(200));
                pause.setOnFinished(_ -> MidiManager.getInstance().stopNote(note));
                MidiManager.getInstance().playNote(note);
                pause.play();
            }

            NoteView noteView = new NoteView(note);
            noteView.setLayoutX(snappedX);
            noteView.setLayoutY(snappedY);

            ArrayList<NoteView> noteViews = new ArrayList<>();
            noteViews.add(noteView);
            CommandManager.getInstance().executeCommand(new AddNotesCommand(noteViews));
            PlaybackManager.getInstance().updateNotes();
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

    public void scroll(KeyCode keyCode) {
        double dx = 0;
        double dy = 0;
        switch (keyCode) {
            case UP -> dy = 10;
            case DOWN -> dy = -10;
            case LEFT -> dx = 20;
            case RIGHT -> dx = -10;
        }

        scrollBy(dx, dy);
    }

    public void scrollBy(double dx, double dy) {
        BasicScrollPane scroll = mainPane.getPianoGridPane().getPianoGridScrollPane();

        double vScrollSpeed = 0.0005;
        double hScrollSpeed = 0.01;

        double h = scroll.getHvalue() - dx * hScrollSpeed;
        double v = scroll.getVvalue() - dy * vScrollSpeed;

        scroll.setHvalue(clamp(h, 0, 1));
        scroll.setVvalue(clamp(v, 0, 1));
    }

    public void moveNoteViews(KeyCode keyCode) {
        ArrayList<NoteView> selectedNoteView = new ArrayList<>(SelectionManager.getInstance().getSelectedNotes());
        NoteView mostUp = null;
        NoteView mostDown = null;
        NoteView mostLeft = null;
        for (NoteView note : selectedNoteView) {
            if (mostUp == null || note.getLayoutY() < mostUp.getLayoutY()) mostUp = note;
            if (mostDown == null || note.getLayoutY() > mostDown.getLayoutY()) mostDown = note;
            if (mostLeft == null || note.getLayoutX() < mostLeft.getLayoutX()) mostLeft = note;
        }

        double xStep = CELL_WIDTH * PianoGridPane.zoomX.get() * 4.0 / GridRenderer.getInstance().getGridProperty().get();
        double yStep = CELL_HEIGHT * PianoGridPane.zoomY.get();
        assert mostLeft != null;
        double xShift = 0;
        double yShift = 0;

        switch (keyCode) {
            case UP -> { if (mostUp.getLayoutY() > 0) yShift = -yStep; }
            case DOWN -> { if(mostDown.getLayoutY() < getHeight()-yStep) yShift = yStep; }
            case LEFT -> {
                if(mostLeft.getLayoutX() > 0) {
                    if (mostLeft.getLayoutX() - xStep < 0) xShift = -mostLeft.getLayoutX();
                    else xShift = -xStep;
                }
            }
            case RIGHT -> xShift = xStep;
        }

        HashMap<NoteView, Double[]> moveMap = new HashMap<>();
        for (NoteView noteView : SelectionManager.getInstance().getSelectedNotes()) {
            double oldCol = GridRenderer.getInstance().xAsColumn(noteView.getLayoutX());
            double oldRow = GridRenderer.getInstance().yAsRow(noteView.getLayoutY());
            noteView.setLayoutX(noteView.getLayoutX() + xShift);
            noteView.setLayoutY(noteView.getLayoutY() + yShift);
            double newCol = GridRenderer.getInstance().xAsColumn(noteView.getLayoutX());
            double newRow = GridRenderer.getInstance().yAsRow(noteView.getLayoutY());

            Double[] pos = new Double[]{newCol, newRow, oldCol, oldRow};
            moveMap.put(noteView, pos);
        }
        CommandManager.getInstance().executeCommand(new MoveNotesCommand(moveMap));

    }
}
