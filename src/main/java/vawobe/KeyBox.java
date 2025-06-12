package vawobe;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import static vawobe.model.manager.NoteManager.OCTAVES;
import static vawobe.model.manager.NoteManager.TONES;

public class KeyBox extends Pane {
    private KeyButton lastActiveButton = null;
    public final static double WIDTH = 120;

    public KeyBox() {
        drawToneNames();
        setPrefWidth(WIDTH);

        setOnMouseDragged(this::onMouseDraggedEvent);
        setOnMouseReleased(_ -> onMouseReleasedEvent());
    }


    protected void drawToneNames() {
        int row = 0;
        for(int octave = OCTAVES.length+1; octave > 1; octave--) {
            for(String tone : TONES) {
                KeyButton keyButton = new KeyButton(tone + octave, row);
                getChildren().add(keyButton);
                keyButton.setLayoutY(row * 25*PianoGridPane.zoomX.get());
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
    private void onMouseReleasedEvent() {
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

    public void updateBox() {
        for(Node n : getChildren()) {
            if(n instanceof KeyButton btn) {
                btn.zoom();

            }
        }
    }
}
