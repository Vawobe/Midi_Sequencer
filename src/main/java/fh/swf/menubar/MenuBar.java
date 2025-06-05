package fh.swf.menubar;

import fh.swf.model.manager.ModeManager;
import javafx.geometry.Insets;
import javafx.scene.control.ToggleGroup;
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

        ToggleGroup toggleGroup = new ToggleGroup();
        DrawButton drawButton = new DrawButton();
        EraseButton eraseButton = new EraseButton();
        SelectButton selectButton = new SelectButton();

        toggleGroup.getToggles().addAll(drawButton, eraseButton, selectButton);
        toggleGroup.selectedToggleProperty().addListener((_,_,newValue) -> {
            if(newValue instanceof ModeButton modeButton)
                ModeManager.getInstance().changeMode(modeButton.getMode());
        });
        drawButton.setSelected(true);

        HBox modeButtons = new HBox(drawButton, selectButton, eraseButton);

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
