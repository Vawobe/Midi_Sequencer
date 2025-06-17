package vawobe;

import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import vawobe.commands.MoveNotesCommand;
import vawobe.commands.RemoveNotesCommand;
import vawobe.commands.SelectNotesCommand;
import vawobe.manager.PlaybackManager;
import vawobe.enums.Modes;
import vawobe.manager.CommandManager;
import vawobe.manager.ModeManager;
import vawobe.manager.NoteManager;
import vawobe.manager.SelectionManager;
import vawobe.render.GridRenderer;
import vawobe.render.NoteRenderer;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Cursor;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.*;

import static vawobe.Main.mainColor;
import static vawobe.Main.mainPane;
import static vawobe.render.GridRenderer.CELL_HEIGHT;
import static vawobe.render.GridRenderer.CELL_WIDTH;

public class NoteView extends Pane {
    @Getter private final NoteViewModel viewModel;

    private double dragStartX;
    private double dragStartY;
    private double startXLayout;
    private double startYLayout;
    private double startWidth;
    private double startColumn;
    private int startRow;
    private boolean resizing = false;
    private boolean dragging = false;
    @Getter private final SimpleBooleanProperty selectedProperty = new SimpleBooleanProperty(false);

    public NoteView(Note note) {
        this.viewModel = new NoteViewModel(note);

        Border normalBorder = new Border(new BorderStroke(mainColor, BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(0.5)));
        Border selectedBorder = new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.DOTTED, new CornerRadii(10), new BorderWidths(1)));

        setBackground(new Background(new BackgroundFill(note.getInstrument().getColor(), new CornerRadii(10), null)));
        setBorder(normalBorder);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(2.0);
        dropShadow.setColor(Color.WHITE);

        double width = note.getLength() * CELL_WIDTH;
        setPrefSize(width*PianoGridPane.zoomX.get(), CELL_HEIGHT*PianoGridPane.zoomY.get());

        selectedProperty.addListener((_,_,newValue) -> {
            setBorder(newValue ? selectedBorder : normalBorder);
            setEffect(newValue ? dropShadow : null);
        });

        viewModel.getInstrumentProperty().addListener((_,_,newValue) ->
                setBackground(new Background(new BackgroundFill(newValue.getColor(), new CornerRadii(10), null))));

        initDragHandler();

        VolumeScale volumeScale = new VolumeScale(this);
        getChildren().add(volumeScale);
        updateNoteSize();
    }

    private void initDragHandler() {
        setOnMouseMoved(event -> {
            if(event.getX() > getWidth() - 5) {
                if(ModeManager.getInstance().getCurrentModeProperty().get() != Modes.ERASE) setCursor(Cursor.E_RESIZE);
            } else {
                setCursor(Cursor.DEFAULT);
            }
        });

        setOnMousePressed(event -> {
            if(ModeManager.getInstance().getCurrentModeProperty().get() != Modes.ERASE) {
                if (event.isPrimaryButtonDown()) {
                    if (event.isControlDown() && event.isAltDown()) NoteRenderer.getInstance().selectAllNotesWithInstrument(viewModel.getInstrumentProperty().get());
                    else if(event.isControlDown() && event.isShiftDown()) mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().setValue(getViewModel().getInstrumentProperty().getValue());
                    else {
                        if(!selectedProperty.get()) {
                            ArrayList<NoteView> noteViews = new ArrayList<>();
                            if(event.isControlDown())
                                noteViews.addAll(SelectionManager.getInstance().getSelectedNotes());
                            noteViews.add(this);
                            Set<NoteView> oldSelection = SelectionManager.getInstance().getSelectedNotes();
                            Set<NoteView> newSelection = new HashSet<>(noteViews);
                            if(!oldSelection.equals(newSelection)) CommandManager.getInstance().executeCommand(new SelectNotesCommand(oldSelection, newSelection));
                        }
                        if (event.getX() > getWidth() - 5) {
                            resizing = true;
                            dragStartX = event.getSceneX();
                            startWidth = getWidth();
                        } else {
                            if (!SelectionManager.getInstance().getSelectedNotes().isEmpty()) {
                                dragStartX = event.getSceneX();
                                dragStartY = event.getSceneY();
                                for (NoteView noteView : SelectionManager.getInstance().getSelectedNotes()) {
                                    noteView.startRow = noteView.getViewModel().getRowProperty().get();
                                    noteView.startColumn = noteView.getViewModel().getColumnProperty().get();
                                    noteView.startXLayout = noteView.getLayoutX();
                                    noteView.startYLayout = noteView.getLayoutY();
                                }
                            }
                        }
                    }
                } else if (event.isSecondaryButtonDown()) {
                    ArrayList<NoteView> noteViews = new ArrayList<>();
                    noteViews.add(this);
                    CommandManager.getInstance().executeCommand(new RemoveNotesCommand(noteViews));
                }
            }
            event.consume();
        });

        setOnMouseDragged(event -> {
            if(ModeManager.getInstance().getCurrentModeProperty().get() != Modes.ERASE) {
                if(event.isPrimaryButtonDown()) {
                    if (!resizing) dragging = true;

                    double zoomedCellWidth = CELL_WIDTH * PianoGridPane.zoomX.get();
                    double gridWidth = 4.0 / GridRenderer.getInstance().getGridProperty().get();

                    if (resizing) {
                        double minWidth = CELL_WIDTH * PianoGridPane.zoomX.get() * gridWidth;
                        double deltaX = event.getSceneX() - dragStartX;
                        double snappedX = GridRenderer.getInstance().snapXLayout(deltaX) + zoomedCellWidth;
                        double newWidth = Math.max(minWidth, startWidth + snappedX);
                        double length = newWidth / (zoomedCellWidth);

                        setPrefWidth(newWidth);
                        viewModel.getLengthProperty().set(length);
                    } else if (dragging) {
                        ArrayList<NoteView> selectedNoteView = new ArrayList<>(SelectionManager.getInstance().getSelectedNotes());
                        if (!selectedNoteView.isEmpty()) {
                            NoteView mostUp = null;
                            NoteView mostDown = null;
                            NoteView mostLeft = null;
                            for (NoteView note : selectedNoteView) {
                                if (mostUp == null || note.getLayoutY() < mostUp.getLayoutY()) mostUp = note;
                                if (mostDown == null || note.getLayoutY() > mostDown.getLayoutY()) mostDown = note;
                                if (mostLeft == null || note.getLayoutX() < mostLeft.getLayoutX()) mostLeft = note;
                            }

                            double maxLeftShift = -mostLeft.startXLayout;
                            double maxUpShift = -mostUp.startYLayout;
                            double maxDownShift = NoteRenderer.getInstance().getHeight() - mostDown.startYLayout
                                    - (CELL_HEIGHT * PianoGridPane.zoomY.get());

                            double snappedDragX = GridRenderer.getInstance().snapXLayout(event.getSceneX());
                            double snappedDragStartX = GridRenderer.getInstance().snapXLayout(dragStartX);
                            double xShift = Math.max(maxLeftShift, snappedDragX - snappedDragStartX);

                            double snappedDragY = GridRenderer.getInstance().snapYToGrid(event.getSceneY());
                            double snappedDragStartY = GridRenderer.getInstance().snapYToGrid(dragStartY);
                            double yShift = Math.clamp(snappedDragY - snappedDragStartY, maxUpShift, maxDownShift);

                            for (NoteView noteView : SelectionManager.getInstance().getSelectedNotes()) {
                                double newXLayout = noteView.startXLayout + xShift;
                                double newYLayout = noteView.startYLayout + yShift;

                                noteView.setLayoutX(newXLayout);
                                noteView.setLayoutY(newYLayout);
                            }
                        }
                    }
                }
            }
            event.consume();
        });


        setOnMouseReleased(event -> {

            if(event.getButton() == MouseButton.PRIMARY) {
                if (ModeManager.getInstance().getCurrentModeProperty().get() == Modes.ERASE) {
                    ArrayList<NoteView> noteViews = new ArrayList<>();
                    noteViews.add(this);
                    CommandManager.getInstance().executeCommand(new RemoveNotesCommand(noteViews));
                } else {
                    if (dragging) {
                        HashMap<NoteView, Double[]> moveMap = new HashMap<>();
                        for (NoteView noteView : SelectionManager.getInstance().getSelectedNotes()) {
                            double newCol = GridRenderer.getInstance().xAsColumn(noteView.getLayoutX());
                            double newRow = GridRenderer.getInstance().yAsRow(noteView.getLayoutY());
                            double oldCol = noteView.startColumn;
                            double oldRow = noteView.startRow;

                            Double[] pos = new Double[]{newCol, newRow, oldCol, oldRow};
                            moveMap.put(noteView, pos);
                        }
                        CommandManager.getInstance().executeCommand(new MoveNotesCommand(moveMap));
                    }
                    SelectionManager.getInstance().getSelectedNotes().forEach(NoteView::resetAttributes);
                }

                viewModel.updateNote();
                PlaybackManager.getInstance().updateNotes();
            }
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
        startRow = 0;
        startColumn = 0;
    }

    public void move(double row, double column) {
        viewModel.getRowProperty().set((int) row);
        viewModel.getColumnProperty().set(column);

        viewModel.calculateMidiNote();
        viewModel.updateNote();
        updateNoteSize();
        NoteManager.getInstance().getNotesList().sort(Comparator.comparingDouble(Note::getColumn));
        PlaybackManager.getInstance().updateNotes();
    }
}
