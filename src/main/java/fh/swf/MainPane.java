package fh.swf;

import fh.swf.menubar.MenuBar;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class MainPane extends BorderPane {
    private final PianoPane pianoPane;
    private final MenuBar menuBar;

    public MainPane() {
        setBackground(new Background(new BackgroundFill(Color.GREEN, null, null)));
        pianoPane = new PianoPane();
        menuBar = new MenuBar();
        setTop(menuBar);
        setCenter(pianoPane);
    }
}
