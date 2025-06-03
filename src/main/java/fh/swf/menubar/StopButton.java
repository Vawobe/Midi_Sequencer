package fh.swf.menubar;

import fh.swf.controller.PlaybackController;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StopButton extends MenuButton{
    public StopButton() {
        super();
        Rectangle rectangle = new Rectangle(10,10);
        rectangle.setFill(Color.ORANGE);
        setGraphic(rectangle);

        setOnAction(_ -> PlaybackController.getInstance().stopPlayback());
    }
}
