package vawobe.menubar.other;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

import static vawobe.Main.mainColor;

@Getter
public class TitleBox extends HBox {
    private final TextField titleTextField;

    public TitleBox() {
        Label label = new Label("Title");
        label.setTextFill(Color.LIGHTGRAY);

        titleTextField = new TextField("Untitled");
        titleTextField.setPrefWidth(100);
        titleTextField.setBackground(new Background(new BackgroundFill(mainColor, null, null)));
        titleTextField.textProperty().addListener((_,_,newValue) -> titleTextField.setTooltip(new Tooltip(newValue)));
        titleTextField.setStyle("-fx-text-fill: white;") ;

        getChildren().addAll(label, titleTextField);
        setAlignment(Pos.BASELINE_LEFT);
        setBorder(new Border(new BorderStroke(
                Color.LIGHTGRAY,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(0, 0, 0.5, 0)
        )));
    }
}
