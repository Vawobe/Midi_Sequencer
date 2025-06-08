package vawobe.menubar.player;

import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
public class PlayerButtonBox extends HBox {
    public PlayerButtonBox() {
        super(new PlayButton(), new StopButton());
    }
}
