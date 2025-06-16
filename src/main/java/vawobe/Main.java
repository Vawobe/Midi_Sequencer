package vawobe;

import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import vawobe.manager.EventManager;
import vawobe.manager.MidiManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import vawobe.manager.SelectionManager;

import java.util.Objects;

/**
 * TODO:
 * (Auto scroll und key guide)?
 * Performance?
 * Bereich zum Loopen auswählen?
 */
public class Main extends Application {
    public static MainPane mainPane;
    public static final Color mainColor = Color.web("#35363a");
    public static final Color gridLineColor = Color.web("#2b2c2f");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        mainPane = new MainPane();
        Scene scene = new Scene(mainPane);
        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            TextField titleTextField = mainPane.getMenuBar().getTitleBox().getTitleTextField();
            TextField bpmTextField = mainPane.getMenuBar().getBpmField().getBpmTextField();
            if(titleTextField.isFocused() || bpmTextField.isFocused()) return;
            EventManager.onKeyPressedEvent(event);
            event.consume();
        });
        scene.addEventFilter(ScrollEvent.SCROLL, event -> {
            if(!SelectionManager.getInstance().getSelectedNotes().isEmpty() && event.isAltDown()) {
                EventManager.onScrollEvent(event);
                event.consume();
            }
        });
        scene.addEventFilter(KeyEvent.KEY_RELEASED, EventManager::onKeyReleasedEvent);

        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icon.png"))));

        primaryStage.setScene(scene);
        primaryStage.setTitle("MIDI Sequencer");
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        MidiManager.getInstance().close();
    }
}