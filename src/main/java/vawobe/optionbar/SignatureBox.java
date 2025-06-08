package vawobe.optionbar;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class SignatureBox extends HBox {
    private final SignatureComboBox signatureComboBox;

    public SignatureBox() {
        Label label = new Label("Time signature");
        label.setTextFill(Color.LIGHTGRAY);

        signatureComboBox = new SignatureComboBox();

        getChildren().addAll(label, signatureComboBox);
        setAlignment(Pos.BASELINE_LEFT);
        setBorder(new Border(new BorderStroke(
                Color.WHITE,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(0, 0, 0.5, 0)
        )));
    }
}
