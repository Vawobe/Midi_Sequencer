package vawobe.menubar.zoom;

import vawobe.menubar.MenuButton;
import javafx.event.ActionEvent;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

import static vawobe.PianoGridPane.*;

public class ZoomInButton extends MenuButton {
    public ZoomInButton() {
        super();
        SVGPath icon = new SVGPath();
        icon.setContent("M7 10H10M10 10H13M10 10V7M10 10V13M15 15L21 21M10 17C6.13401 17 3 13.866 3 10C3 6.13401 6.13401 3 10 3C13.866 3 17 6.13401 17 10C17 13.866 13.866 17 10 17Z");
        icon.setStroke(Color.LIGHTGRAY);
        icon.setFill(Color.TRANSPARENT);
        setGraphic(icon);

        hoverProperty().addListener((_,_,newValue) -> icon.setStroke(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setTooltip(new Tooltip("Zoom in"));
        setOnAction(this::zoomInAction);
    }

    private void zoomInAction(ActionEvent event) {
        double zoomFactor = 1.1;
        zoomX.set(Math.clamp(zoomX.get() * zoomFactor, MIN_X_ZOOM, MAX_ZOOM));
        zoomY.set(Math.clamp(zoomX.get() * zoomFactor, MIN_Y_ZOOM, MAX_ZOOM));
    }
}
