package vawobe;

import vawobe.render.GridRenderer;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.layout.*;
import lombok.Getter;

import static javafx.scene.input.ScrollEvent.SCROLL;

public class PianoGridPane extends GridPane {
    public static SimpleDoubleProperty zoomX = new SimpleDoubleProperty(1.0);
    public static SimpleDoubleProperty zoomY = new SimpleDoubleProperty(1.0);
    public static final double MIN_X_ZOOM = 0.05;
    public static final double MIN_Y_ZOOM = 0.38;
    public static final double MAX_ZOOM = 3.0;

    @Getter private final PianoGrid pianoGrid;
    private final KeyBox keyBox;
    private final DrumBox drumBox;

    private final BasicScrollPane keyBoxScrollPane;
    @Getter private final BasicScrollPane pianoGridScrollPane;
    @Getter private final SignatureLine signatureLine;

    public PianoGridPane() {
        super();
        keyBox = new KeyBox();
        drumBox = new DrumBox();
        pianoGrid = new PianoGrid();

        keyBoxScrollPane = new BasicScrollPane();
        pianoGridScrollPane = new BasicScrollPane();
        pianoGridScrollPane.widthProperty().addListener((_,_,_) -> applyZoom());

        keyBoxScrollPane.setContent(keyBox);
        pianoGridScrollPane.setContent(pianoGrid);

        setPrefHeight(500);
        setHgrow(pianoGridScrollPane, Priority.ALWAYS);

        keyBoxScrollPane.vvalueProperty().bindBidirectional(pianoGridScrollPane.vvalueProperty());

        signatureLine = new SignatureLine(this);

        add(signatureLine,1,0);
        add(keyBoxScrollPane,0,1);
        add(pianoGridScrollPane, 1, 1);

        zoomX.addListener(_ -> applyZoom());
        zoomY.addListener(_ -> applyZoom());

        keyBoxScrollPane.setMinWidth(KeyBox.WIDTH);
        pianoGridScrollPane.addEventFilter(SCROLL, event -> {
            if(!event.isControlDown()) {
                if (event.getDeltaX() != 0) {
                    event.consume();

                    double hValue = pianoGridScrollPane.getHvalue();
                    double delta = -event.getDeltaX() / pianoGridScrollPane.getContent().getBoundsInLocal().getWidth();
                    double newHValue = hValue + delta;
                    newHValue = Math.clamp(newHValue,0, 1);
                    pianoGridScrollPane.setHvalue(newHValue);
                }
                event.consume();
            }
        });

        pianoGridScrollPane.addEventFilter(SCROLL, event -> {
            if(!event.isControlDown()) {
                if (event.getDeltaY() != 0) {
                    double vValue = pianoGridScrollPane.getVvalue();
                    double delta = -event.getDeltaY() / pianoGridScrollPane.getContent().getBoundsInLocal().getHeight();
                    double newVValue = vValue + delta;
                    pianoGridScrollPane.setVvalue(Math.max(0, Math.min(1, newVValue)));

                    event.consume();
                }
            }
        });

        pianoGridScrollPane.addEventFilter(SCROLL, event -> {
            if(event.isControlDown()) {
                double zoomFactor = 1.1;
                if(event.getDeltaY() < 0) {
                    zoomFactor = 1/zoomFactor;
                }
                zoomX.set(Math.clamp(zoomX.get() * zoomFactor, MIN_X_ZOOM, MAX_ZOOM));
                zoomY.set(Math.clamp(zoomX.get() * zoomFactor, MIN_Y_ZOOM, MAX_ZOOM));
//                applyZoom();

                event.consume();
            }
        });

    }

    public void changeKeyBox() {
        keyBoxScrollPane.setContent(keyBoxScrollPane.getContent() == drumBox ? keyBox : drumBox);
    }

    private void applyZoom() {
        GridRenderer.getInstance().updateGridSize();
        keyBox.updateBox();
        drumBox.updateBox();
        signatureLine.drawLines();

        pianoGrid.requestLayout();
        keyBox.requestLayout();
    }
}
