package vawobe;

import vawobe.menubar.MenuBar;
import vawobe.optionbar.OptionBar;
import javafx.scene.layout.*;
import lombok.Getter;

import static vawobe.Main.mainColor;

@Getter
public class MainPane extends BorderPane {
    private final MenuBar menuBar;
    private final OptionBar optionBar;
    private final PianoGridPane pianoGridPane;

    public MainPane() {
        setBackground(new Background(new BackgroundFill(mainColor, null, null)));
        menuBar = new MenuBar();
        optionBar = new OptionBar();
        pianoGridPane = new PianoGridPane();

        setTop(menuBar);
        setBottom(optionBar);
        setCenter(pianoGridPane);
    }
}
