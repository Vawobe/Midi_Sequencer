package vawobe.menubar;


import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class MenuButton extends Button {
    public MenuButton() {
        setPrefSize(15, 15);
        setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null, null)));
        hoverProperty().addListener((obs,oldV,newValue) -> {
            if(newValue) setBackground(new Background(new BackgroundFill(Color.web("#000", 0.25), new CornerRadii(5), null)));
            else setBackground(new Background(new BackgroundFill(Color.TRANSPARENT,null, null)));
        });
    }
}
