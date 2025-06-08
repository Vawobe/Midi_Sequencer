package vawobe.menubar.player;

import vawobe.controller.PlaybackController;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class PlayButton extends MenuButton {
    private final Polygon playSymbol;
    private final HBox pauseSymbol;

    public PlayButton() {
        super();
        setTooltip(new Tooltip("Play"));
        playSymbol = new Polygon();
        playSymbol.getPoints().addAll(
                0.0, 0.0,
                15.0, 7.5,
                0.0, 15.0
        );
        playSymbol.setFill(Color.ORANGE);
        setGraphic(playSymbol);

        pauseSymbol = new HBox(4);
        Rectangle leftBar = new Rectangle(5, 15);
        leftBar.setFill(Color.ORANGE);
        Rectangle rightBar = new Rectangle(5, 15);
        rightBar.setFill(Color.ORANGE);
        pauseSymbol.getChildren().addAll(leftBar, rightBar);

        PlaybackController.getInstance().getIsPlayingProperty().addListener((_,_,_) -> changeGraphic());

        setOnAction(_ -> {
            if(PlaybackController.getInstance().isPlaying()) PlaybackController.getInstance().pausePlayback();
            else PlaybackController.getInstance().startPlayback();
        });
    }

    public void changeGraphic() {
        if(getGraphic() == playSymbol) {
            setGraphic(pauseSymbol);
            setTooltip(new Tooltip("Pause"));
        }
        else {
            setGraphic(playSymbol);
            setTooltip(new Tooltip("Play"));
        }
    }
}
