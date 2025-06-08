package vawobe.save;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import vawobe.Note;

import java.io.*;
import java.util.List;

public class ProjectIO {
    public static void saveProject(Window window, int bpm, int signature, List<Note> notes) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Speichern");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDIFX-Datei", "*.midfx"));
        File file = fileChooser.showSaveDialog(window);

        if(file != null) {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
                ProjectData projectData = new ProjectData(bpm, signature, notes);
                out.writeObject(projectData);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static ProjectData loadProject(Window window) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Laden");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MIDIFX-Datei", "*.midfx"));
        File file = fileChooser.showOpenDialog(window);

        if (file != null) {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
                return (ProjectData) in.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
