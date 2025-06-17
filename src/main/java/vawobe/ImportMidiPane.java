package vawobe;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import vawobe.commands.AddNotesCommand;
import vawobe.manager.PlaybackManager;
import vawobe.enums.Instruments;
import vawobe.menubar.instrument.InstrumentSelector;
import vawobe.manager.CommandManager;
import vawobe.manager.MidiManager;
import vawobe.render.GridRenderer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vawobe.Main.mainColor;

public class ImportMidiPane extends GridPane {
    private final Map<InstrumentSelector, List<Note>> selectorNoteMap = new HashMap<>();
    private static Stage stage = null;
    private final Map<Integer, List<Note>> noteLists;

    public void initializeStage() {
        if(stage == null) {
            stage = new Stage(StageStyle.UNDECORATED);
            stage.setTitle("Import MIDI");
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
        }
    }

    public ImportMidiPane(Map<Integer, List<Note>> noteLists) {
        this.noteLists = noteLists;
        int row = 0;
        setBackground(new Background(new BackgroundFill(mainColor, new CornerRadii(5), null)));
        setPadding(new Insets(10));
        setHgap(10);

        for(Map.Entry<Integer, List<Note>> entry: noteLists.entrySet()) {
            String name = Instruments.getInstrumentByNum(entry.getKey()).getName();

            Label label = new Label(String.format("%s (%d notes)", Instruments.getInstrumentByNum(entry.getKey()).getName(), entry.getValue().size()));
            label.setTextFill(Color.WHITE);
            add(label, 0,row);


            InstrumentSelector instrumentSelector = new InstrumentSelector();
            instrumentSelector.setValue(Instruments.getInstrumentByName(name));
            add(instrumentSelector, 1,row);
            instrumentSelector.setBorder(new Border(new BorderStroke(
                    Color.WHITE,
                    BorderStrokeStyle.SOLID,
                    null,
                    new BorderWidths(0, 0, 0.5, 0)
            )));

            selectorNoteMap.put(instrumentSelector, entry.getValue());
            row++;
        }

        HBox buttonBox = new HBox(5);
        buttonBox.setPadding(new Insets(10,0,0,0));

        Button importButton = new Button("Import");
        importButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, new CornerRadii(5), null)));
        importButton.hoverProperty().addListener((_,_,newValue) ->
                importButton.setBackground(new Background(new BackgroundFill(
                        newValue ? Color.DARKORANGE : Color.ORANGE,
                        new CornerRadii(5), null))));
        importButton.setTextFill(Color.WHITE);
        importButton.setOnAction(_ -> loadNotes());

        Button cancelButton = new Button("Cancel");
        cancelButton.setBackground(new Background(new BackgroundFill(Color.ORANGE, new CornerRadii(5), null)));
        cancelButton.hoverProperty().addListener((_,_,newValue) ->
                cancelButton.setBackground(new Background(new BackgroundFill(
                        newValue ? Color.DARKORANGE : Color.ORANGE,
                        new CornerRadii(5), null))));
        cancelButton.setTextFill(Color.WHITE);
        cancelButton.setOnAction(_ -> stage.close());

        add(buttonBox,0,row);
        GridPane.setColumnSpan(buttonBox,2);

        Region spacer = new Region();
        buttonBox.getChildren().addAll(spacer, importButton, cancelButton);
        HBox.setHgrow(spacer, Priority.ALWAYS);
        GridPane.setHalignment(buttonBox, HPos.RIGHT);


    }

    public void open() {
        initializeStage();
        ImportMidiPane importPane = new ImportMidiPane(noteLists);
        Scene scene = new Scene(importPane);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    private void loadNotes() {
        for (Map.Entry<InstrumentSelector, List<Note>> entry : selectorNoteMap.entrySet()) {
            Instruments selectedInstrument = entry.getKey().getValue();
            int channel = MidiManager.getInstance().addInstrument(selectedInstrument);
            if(channel != -1) {
                List<Note> notes = entry.getValue();
                List<NoteView> noteViews = new ArrayList<>();
                for (Note note : notes) {
                    note.setInstrument(selectedInstrument);
                    noteViews.add(new NoteView(note));
                }
                CommandManager.getInstance().executeCommand(new AddNotesCommand(noteViews));
                PlaybackManager.getInstance().updateNotes();
                GridRenderer.getInstance().updateGridSize();
            }
        }
        CommandManager.getInstance().clear();
        stage.close();


    }
}
