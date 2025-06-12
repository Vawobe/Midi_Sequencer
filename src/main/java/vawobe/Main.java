package vawobe;

import vawobe.model.manager.MidiManager;
import vawobe.render.NoteRenderer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * TODO:
 * Taste in Instrument Farbe blinken lassen, solange der Ton gespielt wird?
 * Export -> .wav.mp3???
 * Redo und Undo
 * Noten Lautstärke
 * Info Button
 * (Auto scroll und key guide)?
 * Performance beim weit rauszoomen und Note einfügen?
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
        scene.setOnKeyPressed(event -> NoteRenderer.getInstance().onKeyPressedEvent(event));
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