package vawobe;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import static vawobe.Main.mainPane;

public class HelpPane extends BorderPane {
    public HelpPane() {
        Button closeButton = new Button("✖");
        closeButton.setTextFill(Color.LIGHTGRAY);
        closeButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        closeButton.hoverProperty().addListener((_,_,newValue) ->
                closeButton.setTextFill(newValue ? Color.WHITE : Color.LIGHTGRAY));
        closeButton.setOnAction(_ -> mainPane.setRight(null));

        HBox topBar = new HBox();
        topBar.getChildren().add(closeButton);
        topBar.setAlignment(Pos.TOP_RIGHT);
        setTop(topBar);

        VBox content = new VBox();

        Text text = new Text("Hilfe \uD83D\uDE2D");
        text.setFill(Color.WHITE);
        text.setFont(Font.font(25));
        content.getChildren().addAll(
                text
        );
        setCenter(content);
    }
}
