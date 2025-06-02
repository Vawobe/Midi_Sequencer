package fh.swf;

import fh.swf.enums.Instruments;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;

import static fh.swf.Main.mainPane;

public class InstrumentSelector extends ComboBox<Instruments> {
    public InstrumentSelector() {
        super();
        getItems().addAll(Instruments.values());
        setValue(Instruments.values()[1]);

        setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Instruments item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    setText(item.getName());
                    setDisable(item.getNum() == -1);
                    setStyle(item.getNum() == -1 ? "-fx-font-weight: bold; -fx-underline: true;" : "");

                }
            }
        });

        setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Instruments item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getName());
            }
        });

        valueProperty().addListener((_, oldValue, newValue) -> {
            if(newValue == Instruments.DRUMS) {
                MidiManager.getInstance().changeChannel(9);
                mainPane.getPianoPane().changeKeyBox();
            }
            else {
                MidiManager.getInstance().changeChannel(0);
                MidiManager.getInstance().changeInstrument(newValue);
                if(oldValue == Instruments.DRUMS) {
                    mainPane.getPianoPane().changeKeyBox();
                }
            }
        });
    }
}
