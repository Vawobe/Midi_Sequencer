package vawobe.manager;

import javafx.geometry.Point2D;
import vawobe.CopiedNote;
import vawobe.Note;
import vawobe.NoteView;
import vawobe.PianoGridPane;
import vawobe.commands.AddNotesCommand;
import vawobe.commands.RemoveNotesCommand;
import vawobe.render.GridRenderer;
import vawobe.render.NoteRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static vawobe.render.GridRenderer.CELL_HEIGHT;

public class ClipboardManager {
    private static ClipboardManager instance;
    private final List<CopiedNote> clipboard;

    public static ClipboardManager getInstance() {
        if(instance == null) {
            instance = new ClipboardManager();
        }
        return instance;
    }

    private ClipboardManager() {
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

        double clickSnap = GridRenderer.getInstance().snapXLayout(xInPane);

        Optional<CopiedNote> minNoteView = clipboard.stream()
                .min(Comparator.comparingDouble(CopiedNote::getLayoutX));

        double xShift = 0;
        if(minNoteView.isPresent() && !preserveColumn)
            xShift = clickSnap - minNoteView.get().getLayoutX();

        SelectionManager.getInstance().getSelectedNotes().clear();
        List<NoteView> noteViews = new ArrayList<>();
        for (CopiedNote original : clipboard) {
            double xLayout = original.getLayoutX() + xShift;
            double column = GridRenderer.getInstance().xAsColumn(xLayout);
            int row = original.getRow();

            Note note = new Note(column, row, original.getLength(), MidiManager.getInstance().getInstrumentChannel(original.getInstrument()), original.getInstrument());
            note.setVelocity(original.getVelocity());

            double yLayout = note.getRow() * CELL_HEIGHT * PianoGridPane.zoomY.get();
            NoteView noteView = new NoteView(note);
            noteView.setLayoutX(xLayout);
            noteView.setLayoutY(yLayout);
            noteViews.add(noteView);

            SelectionManager.getInstance().getSelectedNotes().add(noteView);
        }
        CommandManager.getInstance().executeCommand(new AddNotesCommand(noteViews));
        PlaybackManager.getInstance().updateNotes();
    }
}
