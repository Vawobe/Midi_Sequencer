package vawobe;

import javafx.scene.control.ScrollPane;

public class BasicScrollPane extends ScrollPane {
    public BasicScrollPane() {
        setStyle(
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;" +
            "-fx-background-color: transparent;" +
            "-fx-border-color: transparent;" +
            "-fx-background-insets: 0;" +
            "-fx-padding: 0;"
        );
        setVbarPolicy(ScrollBarPolicy.NEVER);
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setFocusTraversable(false);
    }
}
