package vawobe.save;

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
            } catch (IOException e) {
                // TODO
                System.err.println("Fehler beim Speichern.\n" + e.getMessage());
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
                    // TODO
                    System.err.println("Fehler beim Laden einer MIDFX-Datei.\n" + e.getMessage());
                }
            } else if(name.toLowerCase().endsWith(".mid")) {
                try {
                    ret[1] = MidiIO.importMidi(file);
                    return ret;
                } catch (Exception e) {
                    // TODO
                    System.err.println("Fehler beim Laden einer MID-Datei.\n" + e.getMessage());
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
            } catch (Exception e) {
                // TODO
                System.err.println("Fehler beim Export einer MID-Datei.\n" + e.getMessage());
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
            } catch (Exception e) {
                // TODO
                System.err.println("Fehler beim Export einer WAV-Datei.\n" + e.getMessage());
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
                    System.err.println("Warnung: Temporäre Datei konnte nicht gelöscht werden: " + tempFile.getAbsolutePath());
                }
            } catch (Exception e) {
                // TODO
                System.err.println("Fehler beim Export einer MP3-Datei.\n" + e.getMessage());
            }
        }
    }
}
