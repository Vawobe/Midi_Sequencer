package fh.swf.menubar;

import javafx.scene.control.TextField;

import static fh.swf.Main.mainPane;

public class BPMField extends TextField {
    public BPMField() {
        setPromptText("BPM");
        setPrefWidth(60);
        setText("120");setOnAction(_ -> mainPane.getPianoPane().getPianoGrid().changeBpm());

    }
}
