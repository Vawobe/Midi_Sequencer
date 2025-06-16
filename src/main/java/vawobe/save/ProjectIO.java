package vawobe.save;

import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import javafx.stage.FileChooser;
import vawobe.Note;

import java.io.*;
import java.util.List;

import static vawobe.Main.mainPane;

public class ProjectIO {
    public static void saveProject(int bpm, int signature, List<Note> notes) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Speichern");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDIFX-Datei", "*.midfx"));
        fileChooser.setInitialFileName(String.format("%s.midfx", mainPane.getMenuBar().getTitleBox().getTitleTextField().getText()));
        File file = fileChooser.showSaveDialog(mainPane.getScene().getWindow());

        if(file != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                ProjectData projectData = new ProjectData(bpm, signature, notes);
                out.writeObject(projectData);

                Notifications.create()
                        .title("Speichern erfolgreich")
                        .text("Die Datei " + file + " wurde gespeichert.")
                        .owner(mainPane.getScene().getWindow())
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();
            } catch (IOException e) {
                Notifications.create()
                        .title("Fehler beim Speichern")
                        .text("Die Datei " + file + " konnte nicht gespeichert werden.\n" + e.getMessage())
                        .owner(mainPane.getScene().getWindow())
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();
            }
        }
    }

    public static Object[] loadProject() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Laden");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDIFX-Datei, MIDI-Datei", "*.midfx", "*.mid"));
        File file = fileChooser.showOpenDialog(mainPane.getScene().getWindow());

        if (file != null) {
            String name = file.getName();

            Object[] ret = new Object[2];
            ret[0] = name.substring(0,name.lastIndexOf("."));
            if(name.toLowerCase().endsWith(".midfx")) {
                try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                    ret[1] = in.readObject();
                    return ret;
                } catch (IOException | ClassNotFoundException e) {
                    Notifications.create()
                            .title("Fehler beim Laden")
                            .text("Die Datei " + file + " konnte nicht geladen werden.\n" + e.getMessage())
                            .owner(mainPane.getScene().getWindow())
                            .hideAfter(Duration.seconds(3))
                            .position(Pos.BOTTOM_RIGHT)
                            .showInformation();
                }
            } else if(name.toLowerCase().endsWith(".mid")) {
                try {
                    ret[1] = MidiIO.importMidi(file);
                    return ret;
                } catch (Exception e) {
                    Notifications.create()
                            .title("Fehler beim Laden")
                            .text("Die Datei " + file + " konnte nicht geladen werden.\n" + e.getMessage())
                            .owner(mainPane.getScene().getWindow())
                            .hideAfter(Duration.seconds(3))
                            .position(Pos.BOTTOM_RIGHT)
                            .showInformation();
                }
            }
        }
        return null;
    }

    public static void exportMidi(List<Note> notes){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export MIDI");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDI-Datei", "*.mid"));
        fileChooser.setInitialFileName(String.format("%s.mid", mainPane.getMenuBar().getTitleBox().getTitleTextField().getText()));
        File file = fileChooser.showSaveDialog(mainPane.getScene().getWindow());

        if(file != null) {
            try {
                MidiIO.exportMidi(notes, file.getAbsolutePath());
                Notifications.create()
                        .title("Export erfolgreich")
                        .text("Die Datei " + file + " wurde erfolgreich exportiert.")
                        .owner(mainPane.getScene().getWindow())
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();
            } catch (Exception e) {
                Notifications.create()
                        .title("Fehler beim Export")
                        .text("Die Datei " + file + " konnte nicht exportiert werden.\n" + e.getMessage())
                        .owner(mainPane.getScene().getWindow())
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();
            }
        }
    }

    public static void exportWav(List<Note> notes) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export WAV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("WAV-Datei", "*.wav"));
        fileChooser.setInitialFileName(String.format("%s.wav", mainPane.getMenuBar().getTitleBox().getTitleTextField().getText()));
        File file = fileChooser.showSaveDialog(mainPane.getScene().getWindow());

        if(file != null) {
            try {
                WavExport.exportMIDIToWav(MidiIO.createMidiSequence(notes), file);
                Notifications.create()
                        .title("Export erfolgreich")
                        .text("Die Datei " + file + " wurde erfolgreich exportiert.")
                        .owner(mainPane.getScene().getWindow())
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();
            } catch (Exception e) {
                Notifications.create()
                        .title("Fehler beim Export")
                        .text("Die Datei " + file + " konnte nicht exportiert werden.\n" + e.getMessage())
                        .owner(mainPane.getScene().getWindow())
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();
            }
        }
    }

    public static void exportMP3(List<Note> notes) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export MP3");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3-Datei", "*.mp3"));
        fileChooser.setInitialFileName(String.format("%s.mp3", mainPane.getMenuBar().getTitleBox().getTitleTextField().getText()));
        File file = fileChooser.showSaveDialog(mainPane.getScene().getWindow());
        if(file != null) {
            try {
                File tempFile = File.createTempFile("Midi Sequencer-", "-suffix");
                WavExport.exportMIDIToWav(MidiIO.createMidiSequence(notes), tempFile);

                MP3Exporter.convertWavToMp3(tempFile, file);
                if (!tempFile.delete()) {
                    Notifications.create()
                            .title("Warnung")
                            .text("Temporäre Datei konnte nicht gelöscht werden: " + tempFile.getAbsolutePath())
                            .owner(mainPane.getScene().getWindow())
                            .hideAfter(Duration.seconds(3))
                            .position(Pos.BOTTOM_RIGHT)
                            .showInformation();
                }
                Notifications.create()
                        .title("Export erfolgreich")
                        .text("Die Datei " + file + " wurde erfolgreich exportiert.")
                        .owner(mainPane.getScene().getWindow())
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();

            } catch (Exception e) {
                Notifications.create()
                        .title("Fehler beim Export")
                        .text("Die Datei " + file + " konnte nicht exportiert werden.\n" + e.getMessage())
                        .owner(mainPane.getScene().getWindow())
                        .hideAfter(Duration.seconds(3))
                        .position(Pos.BOTTOM_RIGHT)
                        .showInformation();
            }
        }
    }
}
