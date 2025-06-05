package fh.swf.menubar;

import fh.swf.enums.Modes;
import fh.swf.model.manager.ModeManager;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class EraseButton extends MenuButton {
    public EraseButton() {
        super();
        SVGPath eraserIcon = new SVGPath();
        eraserIcon.setContent("M3 12L12 3L9 0L0 9L3 12Z M3 12H9");
        eraserIcon.setFill(Color.TRANSPARENT);
        eraserIcon.setStroke(Color.BLACK);
        eraserIcon.setStrokeWidth(1.0);
        eraserIcon.setScaleX(1);
        eraserIcon.setScaleY(1);

        setGraphic(eraserIcon);
        setTooltip(new Tooltip("Erase"));

        hoverProperty().addListener((_,_,newValue) -> eraserIcon.setStroke(newValue ? Color.WHITE : Color.BLACK));

        setOnAction(_ -> ModeManager.getInstance().changeMode(Modes.ERASE));
    }

}
