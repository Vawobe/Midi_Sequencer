package vawobe.menubar.saveload;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.Popup;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DropDownButton extends HBox {
    protected final Button button;
    protected final Button menuButton;
    private final MenuButtonPane contextMenu;

    public DropDownButton() {
        setPadding(new Insets(0,0,0,5));
        button = new Button();
        button.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        button.setMinWidth(Region.USE_PREF_SIZE);
        button.setPrefWidth(20);

        contextMenu = new MenuButtonPane();

        Polygon arrow = new Polygon();
        arrow.getPoints().addAll(
                0.0, 0.0,
                8.0, 0.0,
                4.0, 5.0
        );
        arrow.setFill(Color.LIGHTBLUE);

        Popup popup = new Popup();
        popup.getContent().add(contextMenu);
        popup.setAutoHide(true);

        menuButton = new Button();
        menuButton.setGraphic(arrow);
        menuButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        menuButton.setPadding(new Insets(8,0,0,0));
        menuButton.setMinWidth(Region.USE_PREF_SIZE);
        menuButton.setPrefWidth(10);
        menuButton.setTooltip(new Tooltip("Show options"));
        menuButton.setOnAction(_ -> {
            if(popup.isShowing()) popup.hide();
            else {
                Bounds bounds = menuButton.localToScreen(menuButton.getBoundsInLocal());
                popup.show(menuButton, bounds.getMinX(), bounds.getMaxY());
            }
        });
        getChildren().addAll(button, menuButton);
    }

    public final void setOnAction(EventHandler<ActionEvent> eventHandler) { button.setOnAction(eventHandler); }
    public final void setGraphic(Node graphic) { button.setGraphic(graphic);}
    public final void setTooltip(Tooltip tooltip) { button.setTooltip(tooltip); }
    public final void addItems(Button... buttons) { contextMenu.addAll(List.of(buttons));}
}
