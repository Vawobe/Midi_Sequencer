package vawobe.menubar.saveload;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import vawobe.ImportMidiPane;
import vawobe.Note;
import vawobe.NoteView;
import vawobe.commands.LoadCommand;
import vawobe.controller.PlaybackController;
import vawobe.menubar.MenuButton;
import vawobe.model.manager.CommandManager;
import vawobe.render.GridRenderer;
import vawobe.render.NoteRenderer;
import vawobe.save.ProjectData;
import vawobe.save.ProjectIO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static vawobe.Main.mainPane;


public class LoadButton extends MenuButton {
    public LoadButton() {
        super();
        SVGPath icon = new SVGPath();
        icon.setContent("M5.832 9.72V7.128L9.72 3.24h8.424c.648 0 .648 0 .648.648V19.44c0 .648 0 .648-.648.648H6.48c-.648 0-.648 0-.648-.648m0-11.016h5.184V3.24M1.296 14.256h9.72l-3.24 3.24m3.24-3.24-3.24-3.24");
        icon.setStroke(Color.LIGHTBLUE);
        icon.setFill(Color.TRANSPARENT);
        setGraphic(icon);

        setTooltip(new Tooltip("Load"));

        setOnAction(_ -> {
            PlaybackController.getInstance().stopPlayback();
            Object[] data = ProjectIO.loadProject();
            if(data != null) {
                String oldName = mainPane.getMenuBar().getTitleBox().getTitleTextField().getText();
                String newName = (String) data[0];
                mainPane.getMenuBar().getTitleBox().getTitleTextField().setText(newName);
                if (data[1] instanceof ProjectData projectData) {
                    int oldBPM = PlaybackController.getInstance().getBpmProperty().get();
                    int oldSignature = GridRenderer.getInstance().getSignatureProperty().get();

                    List<NoteView> oldNotes = new ArrayList<>();
                    for(Node child : NoteRenderer.getInstance().getChildren())
                        if(child instanceof NoteView noteView) oldNotes.add(noteView);

                    int newBPM = projectData.bpm();
                    int newSignature = projectData.signature();

                    List<NoteView> loadedNotes = new ArrayList<>();
                    for (Note note : projectData.notes()) loadedNotes.add(new NoteView(note));

                    CommandManager.getInstance().executeCommand(
                            new LoadCommand(loadedNotes, oldNotes,
                                    newBPM, oldBPM,
                                    newSignature, oldSignature,
                                    newName, oldName));
                } else {
                    Map<Integer, List<Note>> notes = isRightMap(data[1]);
                    if(notes != null)
                        ImportMidiPane.open(notes);
                }
            }
        });
    }

    public Map<Integer, List<Note>> isRightMap(Object data) {
        if(data instanceof Map<?,?> map) {
            for(Map.Entry<?, ?> entry : map.entrySet()) {
                if(entry.getKey() instanceof Integer && entry.getValue() instanceof List<?> list) {
                    for (Object note : list) {
                        if(note instanceof Note){
                            @SuppressWarnings("unchecked")
                            Map<Integer, List<Note>> notes = (Map<Integer, List<Note>>) data;
                            return notes;
                        }
                    }
                }
            }
        }
        return null;
    }
}
