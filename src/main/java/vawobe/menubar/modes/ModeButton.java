package vawobe.menubar.modes;

import vawobe.enums.Modes;
import vawobe.icons.SvgIcon;
import vawobe.menubar.MenuButton;
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
import lombok.Getter;

public class ModeButton extends MenuButton implements Toggle {
    private final BooleanProperty selected = new SimpleBooleanProperty(false);
    private final ObjectProperty<ToggleGroup> toggleGroup = new SimpleObjectProperty<>(this, "toggleGroup");
    @Getter private final Modes mode;

    public ModeButton(SvgIcon icon, Modes mode) {
        super();
        setAccessibleRole(AccessibleRole.TOGGLE_BUTTON);

        this.mode = mode;
        setGraphic(icon);


        hoverProperty().addListener((obs,oldV,newValue) -> {
            if(!isSelected()) {
                icon.setStroke(newValue ? Color.ANTIQUEWHITE : Color.DARKGRAY);
            } else {
                icon.setStroke(Color.BLACK);
                setBackground(new Background(new BackgroundFill(Color.web("#FFF", 0.25), new CornerRadii(5), null)));
            }
        });

        selected.addListener((obs,oldV,newValue) -> {
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

        toggleGroup.addListener((obs,oldGroup, newGroup) -> {
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


        setOnAction(a -> {
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
