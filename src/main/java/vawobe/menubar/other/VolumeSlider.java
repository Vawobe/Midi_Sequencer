package vawobe.menubar.other;

import javafx.application.Platform;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Region;
import vawobe.model.manager.MidiManager;

import java.util.Locale;
import java.util.Objects;

public class VolumeSlider extends Slider {
    public VolumeSlider() {
        super(0,1,1);
        setPrefWidth(100);
        getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles/volume-slider.css")).toExternalForm());
        getStyleClass().add("volume-slider");
        valueProperty().addListener((_, _, newVal) -> {
            double percent = (newVal.doubleValue() / getMax()) * 100;
            String formattedPercent = String.format(Locale.US, "%.3f", percent);
            String style = String.format("-fx-background-color: linear-gradient(to right, orange 0%%, orange %s%%, lightgray %s%%, lightgray 100%%);", formattedPercent, formattedPercent);
            lookup(".track").setStyle(style);
        });
        Platform.runLater(() -> {
            Region track = (Region) lookup(".track");
            if (track != null) {
                double percent = (getValue() / getMax()) * 100;
                String formattedPercent = String.format(Locale.US, "%.3f", percent);
                String style = String.format("-fx-background-color: linear-gradient(to right, orange 0%%, orange %s%%, lightgray %s%%, lightgray 100%%);", formattedPercent, formattedPercent);
                lookup(".track").setStyle(style);
            }
        });



        valueProperty().addListener((_, _, newVal) -> {
            int volume = (int) (newVal.doubleValue() * 127);
            MidiManager.getInstance().setVolume(volume);
            setTooltip(new Tooltip(Integer.toString((int) (newVal.doubleValue()*100))));
        });
        MidiManager.getInstance().setVolume((int) (getValue() * 127));
        setTooltip(new Tooltip("100"));

    }
}
