package vawobe.menubar.modes;

import vawobe.model.manager.ModeManager;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ModeButtonBox extends HBox {
    public ModeButtonBox() {
        ToggleGroup toggleGroup = new ToggleGroup();
        DrawButton drawButton = new DrawButton();
        EraseButton eraseButton = new EraseButton();
        SelectButton selectButton = new SelectButton();

        toggleGroup.getToggles().addAll(drawButton, eraseButton, selectButton);
        toggleGroup.selectedToggleProperty().addListener((_,_,newValue) -> {
            if(newValue instanceof ModeButton modeButton)
                ModeManager.getInstance().changeMode(modeButton.getMode());
        });

        drawButton.setSelected(true);
        getChildren().addAll(drawButton, selectButton, eraseButton);

        setBorder(new Border(new BorderStroke(
                Color.LIGHTGRAY,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(0, 0.5, 0, 0)
        )));
    }
}
