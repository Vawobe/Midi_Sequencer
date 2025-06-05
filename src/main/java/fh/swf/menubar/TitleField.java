package fh.swf.menubar;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class TitleField extends TextField {
    public TitleField() {
        setPromptText("Titel...");
        setPrefWidth(150);
        textProperty().addListener((_,_,newValue) -> setTooltip(new Tooltip(newValue)));
        setText("Untitled");
        setStyle("-fx-text-fill: white; -fx-prompt-text-fill: white;") ;

        setBackground(new Background(new BackgroundFill(Color.DARKGRAY, new CornerRadii(5), null)));

    }
}
