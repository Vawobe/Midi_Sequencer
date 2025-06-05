package fh.swf.menubar;

import fh.swf.enums.Modes;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.AccessibleRole;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import lombok.Getter;

public class ModeButton extends MenuButton implements Toggle {
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final ObjectProperty<ToggleGroup> toggleGroup = new SimpleObjectProperty<>(this, "toggleGroup");
    private final SVGPath icon;
    @Getter private final Modes mode;

    public ModeButton(String iconContent, Modes mode) {
        super();
        setAccessibleRole(AccessibleRole.TOGGLE_BUTTON);

        this.mode = mode;

        icon = new SVGPath();
        icon.setContent(iconContent);
        icon.setFill(Color.TRANSPARENT);
        icon.setStroke(Color.DARKGRAY);
        icon.setStrokeWidth(1);
        setGraphic(icon);


        hoverProperty().addListener((_,_,newValue) -> {
            if(!isSelected()) {
                icon.setStroke(newValue ? Color.ANTIQUEWHITE : Color.DARKGRAY);
            } else {
                icon.setStroke(Color.BLACK);
                setBackground(new Background(new BackgroundFill(Color.web("#FFF", 0.25), new CornerRadii(5), null)));
            }
        });

        selected.addListener((_,_,newValue) -> {
            if(newValue) {
                icon.setStroke(Color.BLACK);
                setBackground(new Background(new BackgroundFill(Color.web("#FFF", 0.25), new CornerRadii(5), null)));
                ToggleGroup group = getToggleGroup();
                if(group != null && group.getSelectedToggle() != this) {
                    group.selectToggle(this);
                }
            } else {
                icon.setStroke(Color.DARKGRAY);
                setBackground(null);
            }
        });

        toggleGroup.addListener((_,oldGroup, newGroup) -> {
            if(oldGroup != null)
                oldGroup.getToggles().remove(this);
            if(newGroup != null) {
                if(!newGroup.getToggles().contains(this)) {
                    newGroup.getToggles().add(this);
                    if (isSelected()) {
                        newGroup.selectToggle(this);
                    }
                }
            }
        });


        setOnAction(_ -> {
            if(!isSelected()) setSelected(true);
        });
    }

    @Override
    public ToggleGroup getToggleGroup() {
        return toggleGroup.get();
    }

    @Override
    public void setToggleGroup(ToggleGroup toggleGroup) {
        this.toggleGroup.set(toggleGroup);
    }

    @Override
    public ObjectProperty<ToggleGroup> toggleGroupProperty() {
        return toggleGroup;
    }

    @Override
    public boolean isSelected() {
        return selected.get();
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    @Override
    public BooleanProperty selectedProperty() {
        return selected;
    }
}
