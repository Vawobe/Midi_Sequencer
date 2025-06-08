package vawobe.menubar.other;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class VolumeBox extends HBox {
    public VolumeBox(){
        super(5);
        setAlignment(Pos.CENTER_RIGHT);
        setPadding(new Insets(0, 10, 0, 10));

        Text volumeIcon = new Text("\uD83D\uDD0A");
        volumeIcon.setStyle("-fx-font-size: 14px;");
        volumeIcon.setFill(Color.LIGHTGRAY);

        getChildren().addAll(
                volumeIcon,
                new VolumeSlider()
        );
    }
}
