package vawobe.menubar.saveload;

import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import vawobe.Note;
import vawobe.controller.PlaybackController;
import vawobe.menubar.MenuButton;
import vawobe.model.manager.NoteManager;
import vawobe.render.GridRenderer;
import vawobe.save.ProjectData;
import vawobe.save.ProjectIO;


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
            ProjectData data = ProjectIO.loadProject(getScene().getWindow());
            PlaybackController.getInstance().getBpmProperty().set(data.getBpm());
            GridRenderer.getInstance().getSignatureProperty().set(data.getSignature());
            for(Note note : data.getNotes()) {
                NoteManager.getInstance().addNote(note);
            }
        });

    }
}
