package fh.swf.menubar;

import fh.swf.actions.StopPlaybackAction;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class StopButton extends MenuButton{
    private final StopPlaybackAction stopPlaybackAction = new StopPlaybackAction();

    public StopButton() {
        super();
        Rectangle rectangle = new Rectangle(10,10);
        rectangle.setFill(Color.ORANGE);
        setGraphic(rectangle);

        setOnAction(_ -> stopPlaybackAction.use());
    }
}
