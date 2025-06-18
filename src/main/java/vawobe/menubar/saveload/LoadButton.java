package vawobe.menubar.saveload;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.SVGPath;
import vawobe.ImportMidiPane;
import vawobe.Note;
import vawobe.NoteView;
import vawobe.commands.LoadCommand;
import vawobe.icons.LoadIcon;
import vawobe.manager.PlaybackManager;
import vawobe.menubar.MenuButton;
import vawobe.manager.CommandManager;
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
        SVGPath icon = new LoadIcon(1);
        setGraphic(icon);

        setTooltip(new Tooltip("Load"));

        setOnAction(a -> {
            PlaybackManager.getInstance().stopPlayback();
            Object[] data = ProjectIO.loadProject();
            if(data != null) {
                String oldName = mainPane.getMenuBar().getTitleBox().getTitleTextField().getText();
                String newName = (String) data[0];
                mainPane.getMenuBar().getTitleBox().getTitleTextField().setText(newName);
                if (data[1] instanceof ProjectData) {
                    ProjectData projectData = (ProjectData) data[1];
                    int oldBPM = PlaybackManager.getInstance().getBpmProperty().get();
                    int oldSignature = GridRenderer.getInstance().getSignatureProperty().get();

                    List<NoteView> oldNotes = new ArrayList<>();
                    for(Node child : NoteRenderer.getInstance().getChildren())
                        if(child instanceof NoteView) oldNotes.add((NoteView) child);

                    int newBPM = projectData.bpm;
                    int newSignature = projectData.signature;

                    List<NoteView> loadedNotes = new ArrayList<>();
                    for (Note note : projectData.notes) {
                        loadedNotes.add(new NoteView(note));
                    }

                    CommandManager.getInstance().executeCommand(
                            new LoadCommand(loadedNotes, oldNotes,
                                    newBPM, oldBPM,
                                    newSignature, oldSignature,
                                    newName, oldName));
                } else {
                    Map<Integer, List<Note>> notes = isRightMap(data[1]);
                    if(notes != null)
                        new ImportMidiPane(notes).open();
                }
            }
        });
    }

    public Map<Integer, List<Note>> isRightMap(Object data) {
        if(data instanceof Map<?,?>) {
            Map<?,?> map = (Map<?,?>) data;
            for(Map.Entry<?, ?> entry : map.entrySet()) {
                if(entry.getKey() instanceof Integer && entry.getValue() instanceof List<?>) {
                    List<?> list = (List<?>) entry.getValue();
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
