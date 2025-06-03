package fh.swf.menubar;

import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class TitleField extends TextField {
    public TitleField() {
        setPromptText("Titel");
        setPrefWidth(150);
        textProperty().addListener((_,_,newValue) -> setTooltip(new Tooltip(newValue)));
        setText("Untitled");

    }
}
