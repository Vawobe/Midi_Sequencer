package vawobe.menubar.other;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import lombok.Getter;
import vawobe.manager.MidiManager;

import static vawobe.MathClamp.clamp;

public class VolumeSlider extends Pane {
    private final Rectangle track;
    private final Rectangle background;
    private final Circle thumb;
    @Getter private final SimpleDoubleProperty fillPercentageProperty;

    private final double HEIGHT = 8;

    private boolean isPressed = false;

    public VolumeSlider() {
        super();

        background = new Rectangle();
        background.setFill(Color.DARKGRAY);
        background.setArcWidth(HEIGHT);
        background.setArcHeight(HEIGHT);

        track = new Rectangle();
        track.setFill(Color.ORANGE);
        track.setArcWidth(HEIGHT);
        track.setArcHeight(HEIGHT);

        double THUMB_RADIUS = HEIGHT - 2;
        thumb = new Circle(THUMB_RADIUS);
        thumb.setFill(Color.GRAY);
        thumb.setStroke(Color.WHITE);
        thumb.setStrokeWidth(2);

        fillPercentageProperty = new SimpleDoubleProperty(1);

        setPrefWidth(100);
        setMinHeight(HEIGHT+10);

        layoutBoundsProperty().addListener((obs,oldV,newV) -> layoutChildren());
        fillPercentageProperty.addListener((obs,oldV,newV) -> updateThumbAndTrack());

        thumb.hoverProperty().addListener((obs,oldV,newValue) -> changeThumbHover(newValue));
        background.hoverProperty().addListener((obs,oldV,newValue) -> changeThumbHover(newValue));
        track.hoverProperty().addListener((obs,oldV,newValue) -> changeThumbHover(newValue));

        setOnMousePressed(this::handleMouse);
        setOnMouseDragged(this::handleMouse);
        setOnMouseReleased(e -> {
            isPressed = false;
            changeThumbHover(isHover());
        });

        fillPercentageProperty.addListener((obs,oldV,newValue) -> {
            int volume = (int) (newValue.doubleValue() * 127);
            MidiManager.getInstance().setVolume(volume);
            Tooltip.install(this, new Tooltip(Integer.toString((int) (newValue.doubleValue()*100))));
        });
        Tooltip.install(this, new Tooltip("100"));
        getChildren().addAll(background, track, thumb);
    }

    protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();

        background.setWidth(w);
        background.setHeight(HEIGHT);
        background.setLayoutY((h - HEIGHT) / 2);

        updateThumbAndTrack();
    }

    private void updateThumbAndTrack() {
        double percent = clamp(fillPercentageProperty.get(), 0, 1);
        double w = background.getWidth();
        double h = getHeight();

        double thumbX = percent * w;

        thumb.setCenterX(thumbX);
        thumb.setCenterY(h / 2.0);

        track.setWidth(thumbX);
        track.setHeight(HEIGHT);
        track.setLayoutY(background.getLayoutY());
    }

    private void changeThumbHover(boolean isHovering) {
        if(!isPressed) thumb.setFill(isHovering ? Color.web("#666") : Color.GRAY);
    }

    private void handleMouse(MouseEvent event) {

        isPressed = true;
        double mouseX = clamp(event.getX(), 0, background.getWidth());
        fillPercentageProperty.set(mouseX / background.getWidth());
        thumb.setFill(Color.ORANGE);
    }
}
