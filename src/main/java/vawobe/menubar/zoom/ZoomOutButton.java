package vawobe.menubar.zoom;

import vawobe.icons.ZoomOutIcon;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import static vawobe.MathClamp.clamp;
import static vawobe.PianoGridPane.*;

public class ZoomOutButton extends MenuButton {
    public ZoomOutButton() {
        super();
        SVGPath icon = new ZoomOutIcon(1);
        setGraphic(icon);

        hoverProperty().addListener((obs,oldV,newValue) -> icon.setStroke(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setTooltip(new Tooltip("Zoom in"));
        setOnAction(a -> zoomOutAction());
    }

    private void zoomOutAction() {
        double zoomFactor = 0.9;
        zoomX.set(clamp(zoomX.get() * zoomFactor, MIN_X_ZOOM, MAX_ZOOM));
        zoomY.set(clamp(zoomX.get() * zoomFactor, MIN_Y_ZOOM, MAX_ZOOM));
    }
}
