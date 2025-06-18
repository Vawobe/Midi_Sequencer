package vawobe.render;

import vawobe.Note;
import vawobe.NoteView;
import vawobe.PianoGridPane;
import vawobe.manager.PlaybackManager;
import vawobe.manager.NoteManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import lombok.Getter;

import java.util.ArrayList;

import static vawobe.Main.gridLineColor;
import static vawobe.manager.NoteManager.OCTAVES;
import static vawobe.manager.NoteManager.TONES;

public class GridRenderer extends Pane {
    private static GridRenderer instance;

    private final ArrayList<Line> horizontalLines;
    private final ArrayList<Line> verticalLines;

    public static final int CELL_WIDTH = 100;
    public static final int CELL_HEIGHT = 25;

    @Getter private final SimpleIntegerProperty signatureProperty;
    @Getter private final SimpleIntegerProperty strokeAmountProperty;
    @Getter private final SimpleIntegerProperty gridProperty;

    public static GridRenderer getInstance() {
        if(instance == null) {
            instance = new GridRenderer();
        }
        return instance;
    }

    private GridRenderer() {
        setPickOnBounds(false);
        setBackground(new Background(new BackgroundFill(Color.web("#45474c"), null, null)));

        horizontalLines = new ArrayList<>();
        verticalLines = new ArrayList<>();

        signatureProperty = new SimpleIntegerProperty(4);
        strokeAmountProperty = new SimpleIntegerProperty(0);
        gridProperty = new SimpleIntegerProperty(4);
        updateGridSize();

        signatureProperty.addListener((obs,oldV,newV) -> {
            drawVerticalLines();
            PlaybackManager.getInstance().updateNotes();
        });
        strokeAmountProperty.addListener((obs,oldV,newV) -> drawHorizontalLines());
        gridProperty.addListener((obs,oldV,newV) -> drawVerticalLines());
    }

    public void drawGrid() {
        drawVerticalLines();
        drawHorizontalLines();
    }

    private void drawHorizontalLines() {
        getChildren().removeAll(horizontalLines);
        horizontalLines.clear();

        if(getWidth() > 0) {
            for(int row = 1; row <= (TONES.length* OCTAVES.length); row++) {
                double y = row * CELL_HEIGHT * PianoGridPane.zoomY.get();
                Line hLine = new Line(0,y,getWidth(),y);
                hLine.setStroke(gridLineColor);
                hLine.setStrokeWidth(row % 12 == 0 ? 1 : 0.5);
                horizontalLines.add(hLine);
            }
            getChildren().addAll(horizontalLines);
        }
    }

    private void drawVerticalLines() {
        int visibleCells = 0;
        if(getParent() != null) {
            double visibleWidth = ((Pane)getParent().getParent()).getWidth();
            visibleCells = (int) Math.ceil(visibleWidth / (CELL_WIDTH * PianoGridPane.zoomX.get()));
        }
        if(!NoteManager.getInstance().getNotesList().isEmpty()) {
            Note lastNote = NoteManager.getInstance().getNotesList().get(NoteManager.getInstance().getNotesList().size()-1);
            int lastCell = (int) Math.ceil(lastNote.getColumn()+lastNote.getLength() / CELL_WIDTH * PianoGridPane.zoomX.get());
            if(lastCell > visibleCells) visibleCells = lastCell;
        }
        strokeAmountProperty.set(visibleCells/signatureProperty.get()+1);
        changeWidth(signatureProperty.get() * strokeAmountProperty.get() * CELL_WIDTH * PianoGridPane.zoomX.get());

        getChildren().removeAll(verticalLines);
        verticalLines.clear();

        if(getHeight() > 0) {
            double cellsPerQuarter = gridProperty.get()/4.0;
            double colAmount = signatureProperty.get()*strokeAmountProperty.get()*cellsPerQuarter;

            for(int col = 1; col <= colAmount; col++) {
                double x = (col * CELL_WIDTH * PianoGridPane.zoomX.get()) / cellsPerQuarter;
                Line vLine = new Line(x,0,x,getHeight());
                vLine.setStroke(gridLineColor);

                double strokeWidth = 0.5;
                double cellsPerSignature = signatureProperty.get()*cellsPerQuarter;
                if(col % cellsPerSignature == 0) strokeWidth = 2;
                else if(col % (cellsPerSignature/signatureProperty.get())== 0) strokeWidth = 1;
                vLine.setStrokeWidth(strokeWidth);

                verticalLines.add(vLine);
            }
            getChildren().addAll(verticalLines);
        }
    }

    public void updateGridSize() {
        changeWidth(signatureProperty.get() * strokeAmountProperty.get() * CELL_WIDTH * PianoGridPane.zoomX.get());
        setPrefHeight(TONES.length * (OCTAVES.length) * CELL_HEIGHT * PianoGridPane.zoomY.get());

        for(Node node : NoteRenderer.getInstance().getChildren()) {
            if(node instanceof NoteView) {
                ((NoteView)node).updateNoteSize();
            }
        }
        drawGrid();
    }

    private void changeWidth(double width) {
        setPrefWidth(width);
        setWidth(width);
    }

    public double snapXLayout(double xPosition) {
        int cellsPerQuarter = gridProperty.get() / 4;
        double actualCellWidth = (CELL_WIDTH * PianoGridPane.zoomX.get()) / cellsPerQuarter;
        return Math.floor(xPosition / actualCellWidth) * actualCellWidth;
    }

    public double xAsColumn(double x) {
        return x / (CELL_WIDTH*PianoGridPane.zoomX.get());
    }

    public double snapYToGrid(double yPosition) {
        double actualCellHeight = CELL_HEIGHT * PianoGridPane.zoomY.get();
        return Math.floor(yPosition / actualCellHeight) * actualCellHeight;
    }

    public int yAsRow(double y) {
        return (int) (y / (CELL_HEIGHT*PianoGridPane.zoomY.get()));
    }
}
