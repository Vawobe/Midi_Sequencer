package fh.swf;

import fh.swf.render.GridRenderer;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static fh.swf.render.GridRenderer.CELL_WIDTH;

public class SignatureLine extends ScrollPane {
    private final Pane content;
    private final ArrayList<Line> tactLines;
    private final ArrayList<Text> tactNums;

    private final int HEIGHT = 20;

    public SignatureLine(PianoGridPane pianoGridPane) {
        tactLines = new ArrayList<>();
        tactNums = new ArrayList<>();
        prefWidthProperty().bind(pianoGridPane.getPianoGridScrollPane().prefWidthProperty());

        content = new Pane();
        content.prefWidthProperty().bind(pianoGridPane.getPianoGrid().prefWidthProperty());
        setMinHeight(HEIGHT);
        setMaxHeight(HEIGHT);
        content.setPrefHeight(HEIGHT);
        content.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        setContent(content);
        hvalueProperty().bindBidirectional(pianoGridPane.getPianoGridScrollPane().hvalueProperty());

        setVbarPolicy(ScrollBarPolicy.NEVER);
        setHbarPolicy(ScrollBarPolicy.NEVER);

        drawLines();

        GridRenderer.getInstance().getSignatureProperty().addListener((_,_,_) -> drawLines());

        content.getChildren().add(PianoGrid.getPlayhead().getHead());


    }

    public void drawLines() {
        content.getChildren().removeAll(tactLines);
        content.getChildren().removeAll(tactNums);
        tactLines.clear();
        tactNums.clear();

        for(int signature = 0; signature <= GridRenderer.getInstance().getStrokeAmountProperty().get(); signature++) {
            double x = signature * CELL_WIDTH * PianoGridPane.zoomX * (GridRenderer.getInstance().getSignatureProperty().get());
            Line line = new Line(x,0,x,HEIGHT);
            line.setStrokeWidth(2);
            line.setStroke(Color.DARKGRAY);
            tactLines.add(line);
            Text tact = new Text(Integer.toString(signature+1));
            tact.setY(15);
            tact.setX(x+5);
            tactNums.add(tact);
        }

        content.getChildren().addAll(tactLines);
        content.getChildren().addAll(tactNums);
        PianoGrid.getPlayhead().getHead().toFront();
    }
}
