package fh.swf.menubar;

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
        setSpacing(5);
        setPadding(new Insets(10));
        setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, null, null)));

        playButton = new PlayButton();
        HBox playerButtons = new HBox(playButton, new StopButton());

        bpmField = new BPMField();
        instrumentSelector = new InstrumentSelector();

        HBox modeButtons = new HBox(new DrawButton(), new SelectButton(), new EraseButton());

        getChildren().addAll(
                playerButtons,
                bpmField,
                new TitleField(),
                instrumentSelector,
                modeButtons,
                new VolumeBox()
        );
    }

}
