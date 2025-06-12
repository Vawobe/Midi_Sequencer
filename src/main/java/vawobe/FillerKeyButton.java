package vawobe;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class FillerKeyButton extends KeyButton{
    public FillerKeyButton(int row) {
        super(null, row);

        normalBackground = new Background(new BackgroundFill(Color.WHITE, null, null));
        hoverBackground = normalBackground;
        setBackground(normalBackground);
    }

    @Override
    public void playTone() {}
    @Override
    public void stopTone() {}
}
