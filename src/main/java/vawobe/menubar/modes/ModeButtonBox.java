package vawobe.menubar.modes;

import javafx.collections.ObservableList;
import javafx.scene.control.Toggle;
import vawobe.manager.ModeManager;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class ModeButtonBox extends HBox {
    private final ToggleGroup toggleGroup;

    public ModeButtonBox() {
        toggleGroup = new ToggleGroup();
        DrawButton drawButton = new DrawButton();
        EraseButton eraseButton = new EraseButton();
        SelectButton selectButton = new SelectButton();
        DragToScrollButton dragToScrollButton = new DragToScrollButton();

        toggleGroup.getToggles().addAll(drawButton, selectButton, eraseButton, dragToScrollButton);
        toggleGroup.selectedToggleProperty().addListener((obs,oldV,newValue) -> {
            if(newValue instanceof ModeButton)
                ModeManager.getInstance().changeMode(((ModeButton)newValue).getMode());
        });

        drawButton.setSelected(true);
        getChildren().addAll(drawButton, selectButton, eraseButton, dragToScrollButton);

        setBorder(new Border(new BorderStroke(
                Color.LIGHTGRAY,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(0, 0.5, 0, 0)
        )));
    }

    public void toggleNextButton() {
        Toggle selected = toggleGroup.getSelectedToggle();
        ObservableList<Toggle> toggles = toggleGroup.getToggles();
        int currentIndex = toggles.indexOf(selected);
        int nextIndex = (currentIndex + 1) % toggles.size();
        Toggle next = toggles.get(nextIndex);
        toggleGroup.selectToggle(next);
    }
}
