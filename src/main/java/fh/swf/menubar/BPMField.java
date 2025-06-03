package fh.swf.menubar;

import fh.swf.controller.PlaybackController;
import javafx.scene.control.TextField;

public class BPMField extends TextField {
    public BPMField() {
        setPromptText("BPM");
        setPrefWidth(60);
        setText("120");setOnAction(_ -> PlaybackController.getInstance().changeBpm());

    }
}
