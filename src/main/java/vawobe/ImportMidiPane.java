package vawobe;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import vawobe.enums.Instruments;
import vawobe.menubar.instrument.InstrumentSelector;
import vawobe.model.manager.MidiManager;
import vawobe.model.manager.NoteManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportMidiPane extends GridPane {
    private final Map<InstrumentSelector, List<Note>> selectorNoteMap = new HashMap<>();
    private static final Stage stage = new Stage();

    private ImportMidiPane(Map<Integer, List<Note>> noteLists) {
        int row = 0;

        for(Map.Entry<Integer, List<Note>> entry: noteLists.entrySet()) {
            String name = Instruments.getInstrumentByNum(entry.getKey()).getName();

            Label label = new Label(String.format("%s (%d notes)", Instruments.getInstrumentByNum(entry.getKey()).getName(), entry.getValue().size()));
            add(label, 0,row);


            InstrumentSelector instrumentSelector = new InstrumentSelector();
            instrumentSelector.setValue(Instruments.getInstrumentByName(name));
            add(instrumentSelector, 1,row);

            selectorNoteMap.put(instrumentSelector, entry.getValue());
            row++;
        }

        Button importButton = new Button("Import");
        importButton.setOnAction(_ -> loadNotes());
        add(importButton,0,row);
    }

    public static void open(Map<Integer, List<Note>> noteLists) {
        stage.setTitle("Import MIDI");

        ImportMidiPane importPane = new ImportMidiPane(noteLists);

        Scene scene = new Scene(importPane);
        stage.setScene(scene);

        stage.show();
    }

    private void loadNotes() {
        for (Map.Entry<InstrumentSelector, List<Note>> entry : selectorNoteMap.entrySet()) {
            Instruments selectedInstrument = entry.getKey().getValue();
            int channel = MidiManager.getInstance().addInstrument(selectedInstrument);
            if(channel != -1) {
                List<Note> notes = entry.getValue();
                for (Note note : notes) {
                    note.setInstrument(selectedInstrument);
                    NoteManager.getInstance().addNote(note);
                }
            }
        }
        stage.close();
    }
}
