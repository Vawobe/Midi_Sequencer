package fh.swf.menubar;

import fh.swf.controller.PlaybackController;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StopButton extends MenuButton{
    public StopButton() {
        super();
        setTooltip(new Tooltip("Stop"));
        Rectangle rectangle = new Rectangle(13,13);
        rectangle.setFill(Color.ORANGE);
        setGraphic(rectangle);

        setOnAction(_ -> PlaybackController.getInstance().stopPlayback());
    }
}
