package vawobe.menubar.zoom;

import vawobe.icons.ZoomInIcon;
import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import static vawobe.PianoGridPane.*;

public class ZoomInButton extends MenuButton {
    public ZoomInButton() {
        super();
        SVGPath icon = new ZoomInIcon(1);
        setGraphic(icon);

        hoverProperty().addListener((_,_,newValue) -> icon.setStroke(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setTooltip(new Tooltip("Zoom in"));
        setOnAction(_ -> zoomInAction());
    }

    private void zoomInAction() {
        double zoomFactor = 1.1;
        zoomX.set(Math.clamp(zoomX.get() * zoomFactor, MIN_X_ZOOM, MAX_ZOOM));
        zoomY.set(Math.clamp(zoomX.get() * zoomFactor, MIN_Y_ZOOM, MAX_ZOOM));
    }
}
