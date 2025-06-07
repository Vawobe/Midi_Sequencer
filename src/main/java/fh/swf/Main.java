package fh.swf;

import fh.swf.model.manager.MidiManager;
import fh.swf.render.NoteRenderer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * TODO:
 * Schlagzeug??
 * Speichern
 * Laden
 * Automatisches Grid verlängern
 * Takt Markierung
 * gemeinsame verschieben/löschen/kopieren/einfügen
 * Zoom anpassen -> ab bestimmten Punkt nur horizontal Zoomen
 * BPM kaputt
 * Hö zwei Playhead?
 */
public class Main extends Application {
    public static MainPane mainPane;

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