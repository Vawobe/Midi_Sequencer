package fh.swf;

import fh.swf.enums.Instruments;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import lombok.Getter;

import static fh.swf.Main.mainPane;

public class MenuBar extends HBox {
    private boolean isPlaying = false;
    private final Button playButton;
    @Getter private final TextField bpmField;
    @Getter private final TextField titleField;
    @Getter private final InstrumentSelector instrumentSelector;

    public MenuBar() {
        setSpacing(15);
        setPadding(new Insets(10));
        setBackground(new Background(new BackgroundFill(Color.GRAY, null, null)));
        Polygon triangle = new Polygon();
        triangle.getPoints().addAll(
                0.0, 0.0,
                10.0, 5.0,
                0.0, 10.0
        );
        triangle.setFill(Color.ORANGE);

        Rectangle rectangle = new Rectangle(10,10);
        rectangle.setFill(Color.ORANGE);

        playButton = new Button();
        playButton.setGraphic(triangle);
        playButton.setPrefSize(15, 15);
        playButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null, null)));
        playButton.setOnAction(_ -> {
            if(isPlaying) {
                isPlaying = false;
                playButton.setGraphic(triangle);
                mainPane.getPianoPane().getPianoGrid().stopPlayback();
            } else {
                isPlaying = true;
                playButton.setGraphic(rectangle);
                mainPane.getPianoPane().getPianoGrid().startPlayback();
            }
        });


        bpmField = new TextField();
        bpmField.setPromptText("BPM");
        bpmField.setPrefWidth(60);
        bpmField.setText("120");
        bpmField.setOnAction(_ -> {
            try {
                mainPane.getPianoPane().getPianoGrid().changeBpm();
            } catch (NumberFormatException ignored) {}
        });

        titleField = new TextField();
        titleField.setPromptText("Titel");
        titleField.setPrefWidth(150);

        instrumentSelector = new InstrumentSelector();
        instrumentSelector.setValue(Instruments.values()[1]);

        Slider volumeSlider = new Slider(0,1,0.5);
        volumeSlider.setPrefWidth(100);
        volumeSlider.valueProperty().addListener((_, _, newVal) -> {
            int volume = (int) (newVal.doubleValue() * 127);
            MidiManager.getInstance().setVolume(volume);
        });
        MidiManager.getInstance().setVolume((int) (volumeSlider.getValue() * 127));


        Label volumeIcon = new Label("\uD83D\uDD0A");
        volumeIcon.setStyle("-fx-font-size: 14px;");

        HBox volumeBox = new HBox(5, volumeIcon, volumeSlider);
        volumeBox.setAlignment(Pos.CENTER_RIGHT);
        volumeBox.setPadding(new Insets(0, 10, 0, 10));

        getChildren().addAll(
                playButton,
                bpmField,
                titleField,
                instrumentSelector,
                volumeBox
        );
    }

}
