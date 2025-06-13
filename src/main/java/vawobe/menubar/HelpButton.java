package vawobe.menubar;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import vawobe.HelpPane;

import static vawobe.Main.mainPane;

public class HelpButton extends MenuButton{
    public HelpButton() {
        super();
        SVGPath icon = new SVGPath();
        icon.setContent("M5.6 2.6702C6.7768 1.9896 8.1429 1.6 9.6 1.6c4.4182 0 8 3.5818 8 8s-3.5818 8-8 8-8-3.5818-8-8c0-1.4571.3896-2.8232 1.0702-4M6.4 8c0-4.8 6.4-4.8 6.4 0 0 1.6-1.6 3.2-3.2 3.2v1.6m0 1.6v.8");
        icon.setStroke(Color.LIGHTGRAY);
        icon.setFill(Color.TRANSPARENT);
        setGraphic(icon);
        hoverProperty().addListener((_,_,newValue) -> icon.setStroke(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setTooltip(new Tooltip("Help"));

        setOnAction(_ -> helpAction());
    }

    public void helpAction(){
        Node node = mainPane.getRight();
        if(node == null) mainPane.setRight(new HelpPane());
        else mainPane.setRight(null);
    }
}
