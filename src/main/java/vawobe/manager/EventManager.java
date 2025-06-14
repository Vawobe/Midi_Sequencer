package vawobe.manager;

import javafx.geometry.Point2D;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import vawobe.NoteView;
import vawobe.PianoGrid;
import vawobe.SelectionRectangle;
import vawobe.commands.RemoveNotesCommand;
import vawobe.commands.SelectNotesCommand;
import vawobe.enums.Modes;
import vawobe.render.GridRenderer;
import vawobe.render.NoteRenderer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static vawobe.Main.mainPane;

public abstract class EventManager {
    public static void onMouseDraggedEvent(MouseEvent event) {
        if(ModeManager.getInstance().getCurrentModeProperty().get() == Modes.SELECT
                || event.isControlDown()) {
            SelectionRectangle selectionRectangle = PianoGrid.getSelectionRectangle();
            if(selectionRectangle.isVisible()) {
                selectionRectangle.setX(Math.min(selectionRectangle.getClickedX(), event.getX()));
                selectionRectangle.setY(Math.min(selectionRectangle.getClickedY(), event.getY()));
                selectionRectangle.setWidth(Math.abs(event.getX() - selectionRectangle.getClickedX()));
                selectionRectangle.setHeight(Math.abs(event.getY() - selectionRectangle.getClickedY()));
            }
        }
        else if (ModeManager.getInstance().getCurrentModeProperty().get() == Modes.DRAG_TO_SCROLL) {
            Point2D lastPoint = (Point2D) NoteRenderer.getInstance().getUserData();
            if (lastPoint != null) {
                double dx = event.getSceneX() - lastPoint.getX();
                double dy = event.getSceneY() - lastPoint.getY();


                NoteRenderer.getInstance().scrollBy(dx, dy);
                NoteRenderer.getInstance().setUserData(new Point2D(event.getSceneX(), event.getSceneY()));
            }
        }
    }

    public static void onMouseReleasedEvent(MouseEvent event) {
        if(ModeManager.getInstance().getCurrentModeProperty().get() == Modes.SELECT
                || event.getButton() == MouseButton.PRIMARY) {
            SelectionRectangle selectionRectangle = PianoGrid.getSelectionRectangle();
            Set<NoteView> oldSelect = new HashSet<>(SelectionManager.getInstance().getSelectedNotes());
            Set<NoteView> newSelect = new HashSet<>(SelectionManager.getInstance().getSelectedNotes());
            newSelect.addAll(selectionRectangle.getAllNotesInRectangle());
            CommandManager.getInstance().executeCommand(new SelectNotesCommand(oldSelect, newSelect));
            selectionRectangle.setVisible(false);
        }
    }

    public static void onMousePressed(MouseEvent event) {
        if(!event.isControlDown()) {
            Set<NoteView> oldSelection = new HashSet<>(SelectionManager.getInstance().getSelectedNotes());
            CommandManager.getInstance().executeCommand(new SelectNotesCommand(oldSelection, new HashSet<>()));

            switch (ModeManager.getInstance().getCurrentModeProperty().get()) {
                case DRAW -> { if (event.getTarget() == NoteRenderer.getInstance()) NoteRenderer.getInstance().addNote(event.getX(), event.getY()); }
                case SELECT -> {
                    SelectionRectangle selectionRectangle = PianoGrid.getSelectionRectangle();
                    selectionRectangle.setVisible(true);
                    selectionRectangle.setClickedX(event.getX());
                    selectionRectangle.setClickedY(event.getY());
                }
                case DRAG_TO_SCROLL -> NoteRenderer.getInstance().setUserData(new Point2D(event.getSceneX(), event.getSceneY()));
            }
        } else {
            SelectionRectangle selectionRectangle = PianoGrid.getSelectionRectangle();
            selectionRectangle.setVisible(true);
            selectionRectangle.setClickedX(event.getX());
            selectionRectangle.setClickedY(event.getY());
        }
    }

    public static void onKeyPressedEvent(KeyEvent event) {
        if (event.isControlDown()) {
            switch (event.getCode()) {
                case A -> NoteRenderer.getInstance().selectAll();
                case C -> ClipboardManager.getInstance().copySelectedNotes();
                case X -> ClipboardManager.getInstance().cutSelectedNotes();
                case V -> {
                    boolean altPressed = event.isAltDown();
                    ClipboardManager.getInstance().pasteNotes(!altPressed);
                }
                case Z -> CommandManager.getInstance().undo();
                case Y -> CommandManager.getInstance().redo();
                case UP,DOWN,LEFT,RIGHT -> NoteRenderer.getInstance().moveNoteViews(event.getCode());
            }

        } else if(event.isAltDown()) {
            int grid = -1;
            switch (event.getCode()) {
                case NUMPAD1, DIGIT1 -> grid = 4;
                case NUMPAD2, DIGIT2 -> grid = 8;
                case NUMPAD3, DIGIT3 -> grid = 12;
                case NUMPAD4, DIGIT4 -> grid = 16;
                case NUMPAD5, DIGIT5 -> grid = 18;
                case NUMPAD6, DIGIT6 -> grid = 32;
                case NUMPAD7, DIGIT7 -> grid = 48;
                case NUMPAD8, DIGIT8 -> grid = 64;
                case PLUS, ADD -> {}
                case MINUS, SUBTRACT -> {}
            }
            if(grid != -1) GridRenderer.getInstance().getGridProperty().set(grid);
        } else {
            switch (event.getCode()) {
                case DELETE, BACK_SPACE -> {
                    ArrayList<NoteView> notesToDelete = new ArrayList<>();
                    NoteRenderer.getInstance().getChildren().forEach(node -> {
                        if (node instanceof NoteView noteView) {
                            if (noteView.getSelectedProperty().get()) {
                                notesToDelete.add(noteView);
                            }
                        }
                    });
                    CommandManager.getInstance().executeCommand(new RemoveNotesCommand(notesToDelete));
                }
                case SPACE, ENTER -> {
                    if (PlaybackManager.getInstance().isPlaying()) PlaybackManager.getInstance().pausePlayback();
                    else PlaybackManager.getInstance().startPlayback();
                }
                case ESCAPE -> PlaybackManager.getInstance().stopPlayback();
                case UP,DOWN,LEFT,RIGHT -> NoteRenderer.getInstance().scroll(event.getCode());
                case TAB -> mainPane.getMenuBar().getModeButtonBox().toggleNextButton();
            }

        }
    }
}
