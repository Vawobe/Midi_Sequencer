package vawobe.controller;

import javafx.geometry.Point2D;
import vawobe.CopiedNote;
import vawobe.Note;
import vawobe.NoteView;
import vawobe.PianoGridPane;
import vawobe.commands.AddNotesCommand;
import vawobe.commands.RemoveNotesCommand;
import vawobe.model.manager.CommandManager;
import vawobe.model.manager.MidiManager;
import vawobe.model.manager.SelectionManager;
import vawobe.render.GridRenderer;
import vawobe.render.NoteRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static vawobe.render.GridRenderer.CELL_HEIGHT;
import static vawobe.render.GridRenderer.CELL_WIDTH;

public class ClipboardController {
    private static ClipboardController instance;
    private final List<CopiedNote> clipboard;

    public static ClipboardController getInstance() {
        if(instance == null) {
            instance = new ClipboardController();
        }
        return instance;
    }

    private ClipboardController() {
        clipboard = new ArrayList<>();
    }

    public void copySelectedNotes() {
        clipboard.clear();
        SelectionManager.getInstance().getSelectedNotes().forEach(noteView ->
                clipboard.add(new CopiedNote(noteView)));
    }

    public void cutSelectedNotes() {
        copySelectedNotes();
        CommandManager.getInstance().executeCommand(new RemoveNotesCommand(new ArrayList<>(SelectionManager.getInstance().getSelectedNotes())));
    }

    public void pasteNotes(boolean preserveColumn) {
        if (clipboard.isEmpty()) return;

        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        Point2D localMouse = NoteRenderer.getInstance().screenToLocal(mousePoint.getX(), mousePoint.getY());
        double xInPane = localMouse.getX();
        Optional<CopiedNote> minNoteView = clipboard.stream()
                .min(Comparator.comparingDouble(CopiedNote::getLayoutX));
        double xShift = 0;

        if(minNoteView.isPresent())
            xShift = minNoteView.get().getLayoutX() - xInPane;

        SelectionManager.getInstance().getSelectedNotes().clear();

        List<NoteView> noteViews = new ArrayList<>();
        for (CopiedNote original : clipboard) {
            double length = 4.0 / GridRenderer.getInstance().getGridProperty().get();

            double col;

            if(preserveColumn) col = original.getColumn();
            else {
                int cell = (int) Math.ceil((original.getLayoutX() - xShift) / (length * PianoGridPane.zoomX.get() * 100)) - 1;
                col = cell * length;
            }
            int row = original.getRow();

            Note note = new Note(col, row, original.getLength(), MidiManager.getInstance().getInstrumentChannel(original.getInstrument()), original.getInstrument());
            note.setVelocity(original.getVelocity());


            NoteView noteView = new NoteView(note);

            double snappedX = note.getColumn() * CELL_WIDTH * (4.0/GridRenderer.getInstance().getGridProperty().get()) * PianoGridPane.zoomX.get();
            double snappedY = note.getRow() * CELL_HEIGHT * PianoGridPane.zoomY.get();

            noteView.setLayoutX(snappedX);
            noteView.setLayoutY(snappedY);
            noteViews.add(noteView);

            SelectionManager.getInstance().getSelectedNotes().add(noteView);
        }
        CommandManager.getInstance().executeCommand(new AddNotesCommand(noteViews));
        PlaybackController.getInstance().updateNotes();
    }
}
