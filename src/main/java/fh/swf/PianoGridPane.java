package fh.swf;

import fh.swf.render.GridRenderer;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import lombok.Getter;

import static javafx.scene.input.ScrollEvent.SCROLL;

public class PianoGridPane extends GridPane {
    public static double zoomX = 1.0;
    public static double zoomY = 1.0;
    public static final double MIN_X_ZOOM = 0.1;
    public static final double MIN_Y_ZOOM = 0.4;
    public static final double MAX_ZOOM = 3.0;

    @Getter private final PianoGrid pianoGrid;
    private final KeyBox keyBox;
    private final DrumBox drumBox;

    private final ScrollPane keyBoxScrollPane;
    @Getter private final ScrollPane pianoGridScrollPane;
    @Getter private final SignatureLine signatureLine;

    public PianoGridPane() {
        super();
        keyBox = new KeyBox();
        drumBox = new DrumBox();
        pianoGrid = new PianoGrid();

        keyBoxScrollPane = new ScrollPane();
        keyBoxScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        keyBoxScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pianoGridScrollPane = new ScrollPane();
        pianoGridScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pianoGridScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        pianoGridScrollPane.setPannable(true);

        keyBoxScrollPane.setContent(keyBox);
        pianoGridScrollPane.setContent(pianoGrid);

        setPrefHeight(500);
        setHgrow(pianoGridScrollPane, Priority.ALWAYS);

        keyBoxScrollPane.vvalueProperty().bindBidirectional(pianoGridScrollPane.vvalueProperty());

        signatureLine = new SignatureLine(this);

        add(signatureLine,1,0);
        add(keyBoxScrollPane,0,1);
        add(pianoGridScrollPane, 1, 1);

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
                zoomX = Math.clamp(zoomX * zoomFactor, MIN_X_ZOOM, MAX_ZOOM);
                zoomY = Math.clamp(zoomX * zoomFactor, MIN_Y_ZOOM, MAX_ZOOM);
                applyZoom();

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
