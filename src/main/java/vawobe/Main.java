package vawobe;

import vawobe.model.manager.MidiManager;
import vawobe.render.NoteRenderer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * TODO:
 * Schlagzeug??
 * Speichern -> .midfx
 * Export -> .wav, .mp3, .mid?
 * Laden -> .midifx
 * Import -> .mid
 * Redo und Undo
 * Funktionen für copy, paste, cut
 * gemeinsame verschieben/löschen/kopieren/einfügen
 * Noten Lautstärke
 * Show measure number/Show time für den Takt
 * Bei hover über SignatureLine -> Zeit als Tooltip (zb: 00:06.000)
 * Info Button
 * (Auto scroll und key guide)?
 * Metronom?
 * Performance beim weit rauszoomen und Note einfügen?
 */
public class Main extends Application {
    public static MainPane mainPane;
    public static Color mainColor = Color.web("#35363a");
    public static Color gridLineColor = Color.web("#2b2c2f");

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