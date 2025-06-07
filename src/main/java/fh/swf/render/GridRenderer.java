package fh.swf.render;

import fh.swf.*;
import fh.swf.controller.PlaybackController;
import fh.swf.model.manager.NoteManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import lombok.Getter;

import java.util.ArrayList;

import static fh.swf.model.manager.NoteManager.OCTAVES;
import static fh.swf.model.manager.NoteManager.TONES;

public class GridRenderer extends Pane {
    private static GridRenderer instance;

    private final ArrayList<Line> horizontalLines;
    private final ArrayList<Line> verticalLines;

    public static final int CELL_WIDTH = 100;
    public static final int CELL_HEIGHT = 25;
    private final Color gridColor = Color.LIGHTGRAY;

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

        horizontalLines = new ArrayList<>();
        verticalLines = new ArrayList<>();

        signatureProperty = new SimpleIntegerProperty(4);
        strokeAmountProperty = new SimpleIntegerProperty(0);
        gridProperty = new SimpleIntegerProperty(4);
        updateGridSize();

        signatureProperty.addListener((_,_,_) -> {
            drawVerticalLines();
            PlaybackController.getInstance().updateNotes();
        });
        strokeAmountProperty.addListener((_,_,_) -> drawHorizontalLines());
        gridProperty.addListener((_,_,_) -> drawVerticalLines());
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
                double y = row * CELL_HEIGHT * PianoGridPane.zoomY;
                Line hLine = new Line(0,y,getWidth(),y);
                hLine.setStroke(gridColor);
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
            visibleCells = (int) Math.ceil(visibleWidth / (CELL_WIDTH * PianoGridPane.zoomX));
        }
        if(!NoteManager.getInstance().getNotes().isEmpty()) {
            Note lastNote = NoteManager.getInstance().getNotes().getLast();
            int lastCell = (int) Math.ceil(lastNote.getColumn()+lastNote.getLength() / CELL_WIDTH * PianoGridPane.zoomX);
            if(lastCell > visibleCells) visibleCells = lastCell;
        }
        strokeAmountProperty.set(visibleCells/signatureProperty.get()+1);
        changeWidth(signatureProperty.get() * strokeAmountProperty.get() * CELL_WIDTH * PianoGridPane.zoomX);

        getChildren().removeAll(verticalLines);
        verticalLines.clear();

        if(getHeight() > 0) {
            double cellsPerQuarter = gridProperty.get()/4.0;
            double colAmount = signatureProperty.get()*strokeAmountProperty.get()*cellsPerQuarter;

            for(int col = 1; col <= colAmount; col++) {
                double x = (col * CELL_WIDTH * PianoGridPane.zoomX) / cellsPerQuarter;
                Line vLine = new Line(x,0,x,getHeight());
                vLine.setStroke(gridColor);

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
        changeWidth(signatureProperty.get() * strokeAmountProperty.get() * CELL_WIDTH * PianoGridPane.zoomX);
        setPrefHeight(TONES.length * (OCTAVES.length) * CELL_HEIGHT * PianoGridPane.zoomY);

        for(Node node : NoteRenderer.getInstance().getChildren()) {
            if(node instanceof NoteView noteView) {
                noteView.updateNoteSize();
            }
        }
        drawGrid();
    }

    private void changeWidth(double width) {
        setPrefWidth(width);
        setWidth(width);
    }
}
