package fh.swf.menubar;

import fh.swf.InstrumentSelector;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class MenuBar extends HBox {
    private final PlayButton playButton;
    private final BPMField bpmField;
    private final InstrumentSelector instrumentSelector;

    public MenuBar() {
        setSpacing(15);
        setPadding(new Insets(10));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        playButton = new PlayButton();
        bpmField = new BPMField();
        instrumentSelector = new InstrumentSelector();

        getChildren().addAll(
                playButton,
                new StopButton(),
                bpmField,
                new TitleField(),
                instrumentSelector,
                new VolumeBox()
        );
    }

}
