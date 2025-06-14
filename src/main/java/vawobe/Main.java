package vawobe;

import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import vawobe.manager.EventManager;
import vawobe.manager.MidiManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * TODO:
 * Export -> .wav.mp3???
 * Noten Lautstärke
 * Info Button Inhalt
 * (Auto scroll und key guide)?
 * Performance?
 * Bereich zum Loopen auswählen
 * Ctrl+Alt+C zum Aufteilen einer selektierten Note anhand der Grid
 * Alt + Scroll/Alt+ +/- Volume der selektierten Noten ändern
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