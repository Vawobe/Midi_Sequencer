package fh.swf;

import fh.swf.menubar.MenuBar;
import fh.swf.optionbar.OptionBar;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class MainPane extends BorderPane {
    private final MenuBar menuBar;
    private final OptionBar optionBar;
    private final PianoGridPane pianoGridPane;

    public MainPane() {
        setBackground(new Background(new BackgroundFill(Color.DARKGRAY, null, null)));
        menuBar = new MenuBar();
        optionBar = new OptionBar();
        pianoGridPane = new PianoGridPane();

        setTop(menuBar);
        setBottom(optionBar);
        setCenter(pianoGridPane);
    }
}
