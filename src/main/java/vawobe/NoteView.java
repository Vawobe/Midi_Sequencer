package vawobe;

import javafx.scene.effect.DropShadow;
import lombok.Setter;
import vawobe.commands.MoveNotesCommand;
import vawobe.commands.RemoveNotesCommand;
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
    @Getter @Setter private double dragStartX;
    @Getter @Setter private double dragStartY;
    private double startWidth;
    @Getter @Setter private double startColumn;
    @Getter @Setter private int startRow;
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
                    if (event.isControlDown()) {
                        if(event.isAltDown())
                            NoteRenderer.getInstance().selectAllNotesWithInstrument(viewModel.getInstrumentProperty().get());
                        else
                            mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().setValue(getViewModel().getInstrumentProperty().getValue());
                    } else {
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
                if (!resizing) dragging = true;

                double zoomedCellWidth = CELL_WIDTH * PianoGridPane.zoomX.get();
                double zoomedCellHeight = CELL_HEIGHT * PianoGridPane.zoomY.get();

                double gridWidth = 4.0 / GridRenderer.getInstance().getGridProperty().get();

                if (resizing) {
                    double deltaX = event.getSceneX() - dragStartX;
                    double newWidth = Math.max(zoomedCellWidth * gridWidth, startWidth + deltaX);
                    double snappedCells = Math.round(newWidth / (zoomedCellWidth * gridWidth));
                    double beats = snappedCells * gridWidth;
                    newWidth = beats * zoomedCellWidth;

                    setPrefWidth(newWidth);
                    viewModel.getLengthProperty().set(beats);
                } else if (dragging) {
                    if (!SelectionManager.getInstance().getSelectedNotes().isEmpty()) {
                        double deltaX = event.getSceneX() - dragStartX;
                        double deltaY = event.getSceneY() - dragStartY;

                        int deltaCellsX = (int) Math.round(deltaX / (zoomedCellWidth * gridWidth));
                        int deltaCellsY = (int) Math.round(deltaY / zoomedCellHeight);

                        for (NoteView noteView : SelectionManager.getInstance().getSelectedNotes()) {
                            int newRow = noteView.startRow + deltaCellsY;
                            double newColumn = noteView.startColumn + deltaCellsX * gridWidth;

                            double newLayoutX = Math.max(0, newColumn * zoomedCellWidth);
                            double newLayoutY = Math.max(0, newRow * zoomedCellHeight);

                            noteView.setNewLayout(newLayoutX, newLayoutY);
                        }
                    }
                }
            }
            event.consume();
        });


        setOnMouseReleased(event -> {
            if(ModeManager.getInstance().getCurrentModeProperty().get() == Modes.ERASE) {
                ArrayList<NoteView> noteViews = new ArrayList<>();
                noteViews.add(this);
                CommandManager.getInstance().executeCommand(new RemoveNotesCommand(noteViews));
            } else {
                if(dragging) {
                    HashMap<NoteView, Double[]> moveMap = new HashMap<>();
                    for(NoteView noteView : SelectionManager.getInstance().getSelectedNotes()) {
                        double newColumn = noteView.getLayoutX() / (CELL_WIDTH * PianoGridPane.zoomX.get());
                        double newRow = noteView.getLayoutY() / (CELL_HEIGHT * PianoGridPane.zoomY.get());
                        double oldColumn = noteView.getStartColumn();
                        double oldRow = noteView.startRow;
                        Double[] pos = new Double[] {newColumn, newRow, oldColumn, oldRow};
                        moveMap.put(noteView, pos);
                    }
                    CommandManager.getInstance().executeCommand(new MoveNotesCommand(moveMap));
                }
                SelectionManager.getInstance().getSelectedNotes().forEach(NoteView::resetAttributes);
            }

            PlaybackManager.getInstance().updateNotes();
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

        double zoomedCellWidth = CELL_WIDTH * PianoGridPane.zoomX.get();
        double zoomedCellHeight = CELL_HEIGHT * PianoGridPane.zoomY.get();

        double layoutX = column * zoomedCellWidth;
        double layoutY = row * zoomedCellHeight;

        setNewLayout(layoutX, layoutY);

        viewModel.updateNote();
        viewModel.calculateMidiNote();
        viewModel.updateNote();
        NoteManager.getInstance().getNotesList().sort(Comparator.comparingDouble(Note::getColumn));
        PlaybackManager.getInstance().updateNotes();
    }

    public void setNewLayout(double newX, double newY) {
        setLayoutX(newX);
        setLayoutY(newY);
    }
}
