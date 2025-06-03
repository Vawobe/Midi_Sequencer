package fh.swf.menubar;

import fh.swf.model.manager.MidiManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class VolumeBox extends HBox {
    public VolumeBox(){
        super(5);
        setAlignment(Pos.CENTER_RIGHT);
        setPadding(new Insets(0, 10, 0, 10));

        Text volumeIcon = new Text("\uD83D\uDD0A");
        volumeIcon.setStyle("-fx-font-size: 14px;");

        Slider volumeSlider = new Slider(0,1,1);
        volumeSlider.setPrefWidth(100);
        volumeSlider.valueProperty().addListener((_, _, newVal) -> {
            int volume = (int) (newVal.doubleValue() * 127);
            MidiManager.getInstance().setVolume(volume);
            volumeSlider.setTooltip(new Tooltip(Integer.toString((int) (newVal.doubleValue()*100))));
        });
        MidiManager.getInstance().setVolume((int) (volumeSlider.getValue() * 127));
        volumeSlider.setTooltip(new Tooltip("100"));

        getChildren().addAll(
                volumeIcon,
                volumeSlider
        );

    }
}
