package fh.swf;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import lombok.Getter;

import static fh.swf.KeyBox.WIDTH;
import static javafx.scene.input.ScrollEvent.SCROLL;

public class PianoPane extends ScrollPane {
    private double zoom = 1.0;
    private final double MIN_ZOOM = 0.4;
    private final double MAX_ZOOM = 3.0;

    private final ScrollPane gridScrollPane;
    private final KeyBox keyBox;
    private final DrumBox drumBox;

    @Getter private final PianoGrid pianoGrid;

    private final HBox hBox;

    public PianoPane() {
        super();
        keyBox = new KeyBox();
        drumBox = new DrumBox();
        pianoGrid = new PianoGrid();
        hBox = new HBox();

        setPrefSize(700,500);
        setVbarPolicy(ScrollBarPolicy.NEVER);
        setHbarPolicy(ScrollBarPolicy.NEVER);

        gridScrollPane = new ScrollPane(pianoGrid);
        gridScrollPane.prefWidthProperty().bind(widthProperty().subtract(WIDTH));
        gridScrollPane.setVbarPolicy(ScrollBarPolicy.NEVER);
        gridScrollPane.setHbarPolicy(ScrollBarPolicy.NEVER);
        gridScrollPane.setPannable(true);

        hBox.getChildren().addAll(keyBox, gridScrollPane);
        setContent(hBox);

        addEventFilter(SCROLL, event -> {
            if(!event.isControlDown()) {
                if (event.getDeltaX() != 0) {
                    event.consume();

                    double hValue = gridScrollPane.getHvalue();
                    double delta = -event.getDeltaX() / gridScrollPane.getContent().getBoundsInLocal().getWidth();
                    double newHValue = hValue + delta;
                    newHValue = Math.clamp(newHValue,0, 1);
                    gridScrollPane.setHvalue(newHValue);
                }
            }
        });

        gridScrollPane.addEventFilter(SCROLL, event -> {
            if(!event.isControlDown()) {
                if (event.getDeltaY() != 0) {
                    double vValue = getVvalue();
                    double delta = -event.getDeltaY() / getContent().getBoundsInLocal().getHeight();
                    double newVValue = vValue + delta;
                    setVvalue(Math.max(0, Math.min(1, newVValue)));

                    event.consume();
                }
            }
        });

        addEventFilter(SCROLL, event -> {
            if(event.isControlDown()) {
                double zoomFactor = 1.1;
                if(event.getDeltaY() < 0) {
                    zoomFactor = 1/zoomFactor;
                }
                zoom = Math.clamp(zoom * zoomFactor, MIN_ZOOM, MAX_ZOOM);
                applyZoom();

                event.consume();
            }
        });
    }

    public void changeKeyBox() {
        if(hBox.getChildren().getFirst() == drumBox) {
            hBox.getChildren().set(0, keyBox);
        } else {
            hBox.getChildren().set(0, drumBox);
        }
    }

    private void applyZoom() {
        pianoGrid.setZoom(zoom);
        keyBox.setZoom(zoom);
        drumBox.setZoom(zoom);

        pianoGrid.requestLayout();
        keyBox.requestLayout();
    }
}
