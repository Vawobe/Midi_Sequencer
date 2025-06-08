package vawobe.menubar.instrument;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;

@Getter
public class InstrumentBox extends HBox {
    private final InstrumentSelector instrumentSelector;

    public InstrumentBox() {
        Label label = new Label("Instrument");
        label.setTextFill(Color.LIGHTGRAY);

        instrumentSelector = new InstrumentSelector();

        getChildren().addAll(label, instrumentSelector);
        setAlignment(Pos.BASELINE_LEFT);
        setBorder(new Border(new BorderStroke(
                Color.WHITE,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(0, 0, 0.5, 0)
        )));
    }
}
