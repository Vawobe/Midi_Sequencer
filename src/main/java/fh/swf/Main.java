package fh.swf;

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
 * Selektieren und gemeinsame verschieben/löschen/kopieren/einfügen
 * Zoom anpassen -> ab bestimmten Punkt nur horizontal Zoomen
 * 
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