package fh.swf;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import static fh.swf.PianoGrid.OCTAVES;
import static fh.swf.PianoGrid.TONES;

public class KeyBox extends Pane {
    protected double zoom = 1.0;

    private KeyButton lastActiveButton = null;
    public static double WIDTH = 120;

    public KeyBox() {
        drawToneNames();
        setPrefWidth(WIDTH);

        setOnMouseDragged(this::onMouseDraggedEvent);
        setOnMouseReleased(this::onMouseReleasedEvent);
    }


    protected void drawToneNames() {
        double y = 0;
        int row = 0;
        for(int octave = OCTAVES; octave > 1; octave--) {
            for(String tone : TONES) {
                KeyButton keyButton = new KeyButton(tone + octave, row);
                getChildren().add(keyButton);
                keyButton.setLayoutY(row * 25*zoom);
                row++;
            }
        }
    }

    private void onMouseDraggedEvent(MouseEvent event) {
        Node node = pickNode(event.getX(), event.getY());
        if(node instanceof KeyButton kb) {
            if(kb != lastActiveButton) {
                if(lastActiveButton != null) {
                    lastActiveButton.stopTone();
                }
                kb.playTone();
                lastActiveButton = kb;
            }
        } else {
            if(lastActiveButton != null) {
                lastActiveButton.stopTone();
                lastActiveButton = null;
            }
        }
    }
    private void onMouseReleasedEvent(MouseEvent event) {
        if(lastActiveButton != null) {
            lastActiveButton.stopTone();
            lastActiveButton = null;
        }
    }


    private Node pickNode(double x, double y) {
        for(Node child : getChildren()) {
            if(child instanceof KeyButton) {
                if(child.getBoundsInParent().contains(x,y)) {
                    return child;
                }
            }
        }
        return null;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        updateBox();
    }

    protected void updateBox() {
        for(Node n : getChildren()) {
            if(n instanceof KeyButton btn) {
                btn.zoom(zoom);

            }
        }
    }
}
