package fh.swf.menubar;

import fh.swf.controller.PlaybackController;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class BPMField extends TextField {
    public BPMField() {
        setPromptText("BPM");
        setPrefWidth(60);
        setText(String.valueOf(PlaybackController.getInstance().getBpmProperty().get()));
        setOnAction(_ -> {
            PlaybackController.getInstance().getBpmProperty().set(Integer.parseInt(getText()));
            PlaybackController.getInstance().updateNotes();
        });

        setStyle("-fx-text-fill: white; -fx-prompt-text-fill: white;") ;

        setBackground(new Background(new BackgroundFill(Color.DARKGRAY, new CornerRadii(5), null)));
    }
}
