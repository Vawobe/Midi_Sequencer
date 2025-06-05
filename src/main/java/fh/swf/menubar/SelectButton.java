package fh.swf.menubar;

import fh.swf.enums.Modes;
import fh.swf.model.manager.ModeManager;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class SelectButton extends MenuButton {
    public SelectButton() {
        super();
        SVGPath selectIcon = new SVGPath();
        selectIcon.setContent("M2 2 H5 M2 2 V5 M10 2 H13 M13 2 V5 M2 10 V13 M2 13 H5 M13 10 V13 M13 13 H10");
        selectIcon.setStroke(Color.BLACK);
        selectIcon.setStrokeWidth(1.0);

        setGraphic(selectIcon);
        setTooltip(new Tooltip("Select"));

        hoverProperty().addListener((_,_,newValue) -> selectIcon.setStroke(newValue ? Color.WHITE : Color.BLACK));

        setOnAction(_ -> ModeManager.getInstance().changeMode(Modes.SELECT));
    }
}
