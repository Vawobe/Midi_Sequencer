package vawobe;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class VolumeScale extends Pane {
    public VolumeScale(NoteView noteView) {
        Rectangle background = new Rectangle();
        background.setFill(Color.web("#000", 0.25));

        Rectangle track = new Rectangle();
        track.setFill(Color.web("#FFF", 0.25));

        track.widthProperty().bind(noteView.widthProperty().multiply(noteView.getViewModel().getVelocityProperty().divide(100.0)));

        background.widthProperty().bind(noteView.widthProperty().subtract(track.widthProperty()));
        background.heightProperty().bind(track.heightProperty());
        background.translateXProperty().bind(track.widthProperty());
        track.heightProperty().bind(noteView.heightProperty().divide(5));

        visibleProperty().bind(PianoGridPane.zoomY.greaterThan(0.7).and(noteView.getViewModel().getVelocityProperty().isNotEqualTo(100)));
        translateYProperty().bind(PianoGridPane.zoomY.multiply(15));
        getChildren().addAll(background, track);
    }

}
