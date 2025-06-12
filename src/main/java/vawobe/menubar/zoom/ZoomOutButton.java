package vawobe.menubar.zoom;

import vawobe.menubar.MenuButton;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import static vawobe.PianoGridPane.*;

public class ZoomOutButton extends MenuButton {
    public ZoomOutButton() {
        super();
        SVGPath icon = new SVGPath();
        icon.setContent("M7 10H13M15 15L21 21M10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10C17 13.866 13.866 17 10 17Z");
        icon.setStroke(Color.LIGHTGRAY);
        icon.setFill(Color.TRANSPARENT);
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
