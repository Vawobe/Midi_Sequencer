package fh.swf;

import fh.swf.render.NoteRenderer;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Setter @Getter
public class SelectionRectangle extends Rectangle {
    private double clickedX, clickedY;

    public SelectionRectangle() {
        super();

        setStroke(Color.DARKGRAY);
        setStrokeWidth(1.0);
        getStrokeDashArray().addAll(5.0,5.0);

        setFill(Color.web("#000", 0.1));
        visibleProperty().addListener((_,_,newValue) -> {
            if(!newValue) reset();
        });
    }

    public void reset() {
        setX(-1);
        setY(-1);
        setWidth(0);
        setHeight(0);
    }

    public ArrayList<NoteView> getAllNotesInRectangle() {
        ArrayList<NoteView> selectedNotes = new ArrayList<>();

        Bounds selectionBounds = getBoundsInParent();

        for(Node node : NoteRenderer.getInstance().getChildren()) {
            if(node instanceof NoteView note) {
                Bounds noteBounds = note.getBoundsInParent();
                if(selectionBounds.intersects(noteBounds)) {
                    selectedNotes.add(note);
                }
            }
        }
        return selectedNotes;
    }
}
