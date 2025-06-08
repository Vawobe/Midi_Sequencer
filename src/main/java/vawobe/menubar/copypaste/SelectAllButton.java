package vawobe.menubar.copypaste;

import vawobe.NoteView;
import vawobe.menubar.MenuButton;
import vawobe.render.NoteRenderer;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class SelectAllButton extends MenuButton {
    public SelectAllButton() {
        super();
        Rectangle icon = new Rectangle(15,15);
        icon.setStroke(Color.LIGHTGRAY);
        icon.setFill(Color.TRANSPARENT);
        icon.getStrokeDashArray().addAll(1.0,2.0);
        setTooltip(new Tooltip("Select all"));

        setGraphic(icon);
        hoverProperty().addListener((_,_,newValue) -> icon.setStroke(newValue ? Color.WHITE : Color.LIGHTGRAY));
        setOnAction(this::selectAllAction);
    }
    private void selectAllAction(ActionEvent actionEvent) {
        ArrayList<NoteView> notesToSelect = new ArrayList<>();
        ArrayList<NoteView> selectedNotes = new ArrayList<>();
        for(Node node : NoteRenderer.getInstance().getChildren()) {
            if(node instanceof NoteView note) {
                notesToSelect.add(note);
                if(note.getSelectedProperty().get())
                    selectedNotes.add(note);
            }
        }
        if(notesToSelect.size() == selectedNotes.size()) {
            for(NoteView noteView : notesToSelect) noteView.getSelectedProperty().set(false);
        } else {
            for(NoteView noteView : notesToSelect) noteView.getSelectedProperty().set(true);
        }
    }
}
