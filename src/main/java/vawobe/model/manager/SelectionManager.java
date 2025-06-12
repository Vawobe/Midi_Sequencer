package vawobe.model.manager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import lombok.Getter;
import vawobe.NoteView;

public class SelectionManager {
    private static SelectionManager instance;

    @Getter private final ObservableSet<NoteView> selectedNotes;


    public static SelectionManager getInstance() {
        if(instance == null) instance = new SelectionManager();
        return instance;
    }

    private SelectionManager() {
        selectedNotes = FXCollections.observableSet();
    }
}
