package vawobe.menubar.copypaste;

import vawobe.icons.SelectAllIcon;
import vawobe.menubar.MenuButton;
import vawobe.render.NoteRenderer;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SelectAllButton extends MenuButton {
    public SelectAllButton() {
        super();
        Rectangle icon = new SelectAllIcon(1);
        setTooltip(new Tooltip("Select all"));

        setGraphic(icon);
        hoverProperty().addListener((_,_,newValue) -> icon.setStroke(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setOnAction(_ -> selectAllAction());
    }
    private void selectAllAction() {
        NoteRenderer.getInstance().selectAll();
    }
}
