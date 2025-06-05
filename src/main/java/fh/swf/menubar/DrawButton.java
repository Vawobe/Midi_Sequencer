package fh.swf.menubar;

import fh.swf.enums.Modes;
import fh.swf.model.manager.ModeManager;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

public class DrawButton extends MenuButton {
    public DrawButton() {
        super();
        SVGPath penIcon = new SVGPath();
        penIcon.setContent("M0 0-2.1213-2.1213-7.0711 2.8285-8.8389 6.7175-4.9498 4.9498l6.364-6.364L-.7071-3.5355-2.1213-2.1213ZM-9 7-5 7Z");
        penIcon.setFill(Color.TRANSPARENT);
        penIcon.setStroke(Color.BLACK);
        penIcon.setStrokeWidth(1.0);

        setGraphic(penIcon);
        setTooltip(new Tooltip("Draw"));

        hoverProperty().addListener((_,_,newValue) -> penIcon.setStroke(newValue ? Color.WHITE : Color.BLACK));

        setOnAction(_ -> ModeManager.getInstance().changeMode(Modes.DRAW));
    }
}
