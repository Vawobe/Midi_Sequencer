package vawobe.menubar;

import vawobe.menubar.copypaste.CopyPasteButtonBox;
import vawobe.menubar.instrument.InstrumentBox;
import vawobe.menubar.modes.ModeButtonBox;
import vawobe.menubar.other.BPMField;
import vawobe.menubar.other.TitleBox;
import vawobe.menubar.other.VolumeBox;
import vawobe.menubar.player.PlayerButtonBox;
import vawobe.menubar.saveload.LoadButton;
import vawobe.menubar.saveload.SaveSplitButton;
import vawobe.menubar.zoom.ZoomBox;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import lombok.Getter;

import static vawobe.Main.mainColor;

@Getter
public class MenuBar extends HBox {
    private final InstrumentBox instrumentBox;
    private final TitleBox titleBox;
    private final ModeButtonBox modeButtonBox;
    private final BPMField bpmField;

    public MenuBar() {
        setSpacing(5);
        setPadding(new Insets(10));
        setBackground(new Background(new BackgroundFill(mainColor, null, null)));

        titleBox = new TitleBox();

        instrumentBox = new InstrumentBox();
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        modeButtonBox = new ModeButtonBox();

        bpmField = new BPMField();

        getChildren().addAll(
                new PlayerButtonBox(),
                bpmField,
                titleBox,
                modeButtonBox,
                instrumentBox,
                new CopyPasteButtonBox(),
                new ZoomBox(),
                new SaveSplitButton(),
                new LoadButton(),
                new HelpButton(),
                spacer,
                new VolumeBox()
        );

    }

}
