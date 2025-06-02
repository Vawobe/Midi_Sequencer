package fh.swf.menubar;


import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class MenuButton extends Button {
    public MenuButton() {
        setPrefSize(15, 15);
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null, null)));
    }
}
