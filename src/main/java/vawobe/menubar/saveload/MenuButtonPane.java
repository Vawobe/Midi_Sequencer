package vawobe.menubar.saveload;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.List;

public class MenuButtonPane extends VBox {
    public MenuButtonPane() {
        setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
    }
    public MenuButtonPane(List<Button> menuItems) {
        this();
        addAll(menuItems);
    }

    public void addAll(List<Button> menuItems) {
        for(Button menuItem : menuItems) add(menuItem);
    }

    public void add(Button menuItem) {
        getChildren().add(menuItem);
        initMenuItem(menuItem);
    }

    private void initMenuItem(Button menuItem) {
        menuItem.setAlignment(Pos.BASELINE_LEFT);
        menuItem.setTextFill(Color.DARKGRAY);
        menuItem.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        menuItem.hoverProperty().addListener((_,_,newValue) -> {
            menuItem.setTextFill(newValue ? Color.WHITE : Color.DARKGRAY);
            menuItem.setBackground(new Background(new BackgroundFill(newValue ? Color.ORANGE : Color.TRANSPARENT, null, null)));
            menuItem.prefWidthProperty().bind(widthProperty());
        });
    }
}
