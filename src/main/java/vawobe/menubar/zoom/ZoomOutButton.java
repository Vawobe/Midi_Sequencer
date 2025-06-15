package vawobe.menubar.zoom;

import vawobe.icons.ZoomOutIcon;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import static vawobe.PianoGridPane.*;

public class ZoomOutButton extends MenuButton {
    public ZoomOutButton() {
        super();
        SVGPath icon = new ZoomOutIcon(1);
        setGraphic(icon);

        hoverProperty().addListener((_,_,newValue) -> icon.setStroke(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setTooltip(new Tooltip("Zoom in"));
        setOnAction(_ -> zoomOutAction());
    }

    private void zoomOutAction() {
        double zoomFactor = 0.9;
        zoomX.set(Math.clamp(zoomX.get() * zoomFactor, MIN_X_ZOOM, MAX_ZOOM));
        zoomY.set(Math.clamp(zoomX.get() * zoomFactor, MIN_Y_ZOOM, MAX_ZOOM));
    }
}
