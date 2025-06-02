package fh.swf.menubar;

import fh.swf.actions.StartPlaybackAction;
import fh.swf.actions.PausePlaybackAction;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import static fh.swf.Main.mainPane;

public class PlayButton extends MenuButton {
    private final StartPlaybackAction startPlaybackAction = new StartPlaybackAction();
    private final PausePlaybackAction pausePlaybackAction = new PausePlaybackAction();

    private final Polygon playSymbol;
    private final HBox pauseSymbol;

    public PlayButton() {
        super();
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

        setOnAction(_ -> {
            mainPane.getPianoPane().getPianoGrid().getIsPlaying().set(!mainPane.getPianoPane().getPianoGrid().getIsPlaying().get());

            if(!mainPane.getPianoPane().getPianoGrid().getIsPlaying().get()) pausePlaybackAction.use();
            else startPlaybackAction.use();

        });
    }

    public void changeGraphic() {
        if(getGraphic() == playSymbol) setGraphic(pauseSymbol);
        else setGraphic(playSymbol);
    }
}
