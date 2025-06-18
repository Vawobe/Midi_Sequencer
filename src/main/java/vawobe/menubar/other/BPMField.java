package vawobe.menubar.other;

import vawobe.commands.ChangeBPMCommand;
import vawobe.manager.PlaybackManager;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import lombok.Getter;
import vawobe.manager.CommandManager;

import java.util.function.UnaryOperator;

import static vawobe.Main.mainColor;

@Getter
public class BPMField extends HBox {
    private final TextField bpmTextField;

    public BPMField() {
        Label label = new Label("BPM");
        label.setTextFill(Color.LIGHTGRAY);

        bpmTextField = new TextField(String.valueOf(PlaybackManager.getInstance().getBpmProperty().get()));
        PlaybackManager.getInstance().getBpmProperty().addListener((obs, oldV, newValue) -> {
            if(!newValue.toString().equals(bpmTextField.getText())) {
                bpmTextField.setText(newValue.toString());
            }
        });
        bpmTextField.setPrefWidth(35);
        bpmTextField.setBackground(new Background(new BackgroundFill(mainColor, new CornerRadii(5), null)));
        bpmTextField.setStyle("-fx-text-fill: white;") ;
        bpmTextField.setOnAction(a -> action());

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,3}")) return change;
            return null;
        };
        bpmTextField.setTextFormatter(new TextFormatter<>(filter));

        getChildren().addAll(label, bpmTextField);
        setAlignment(Pos.BASELINE_LEFT);
        setBorder(new Border(new BorderStroke(
                Color.LIGHTGRAY,
                BorderStrokeStyle.SOLID,
                null,
                new BorderWidths(0, 0, 0.5, 0)
        )));

    }

    private void action() {
        if(!bpmTextField.getText().isEmpty() && Integer.parseInt(bpmTextField.getText()) >= 10) {


            CommandManager.getInstance().executeCommand(
                    new ChangeBPMCommand(PlaybackManager.getInstance().getBpmProperty().get(),
                            Integer.parseInt(bpmTextField.getText())));
        } else
            bpmTextField.setText(String.valueOf(PlaybackManager.getInstance().getBpmProperty().get()));
        bpmTextField.getParent().requestFocus();
    }
}
