package vawobe.menubar.instrument;

import vawobe.NoteView;
import vawobe.commands.ChangeInstrumentCommand;
import vawobe.enums.Instruments;
import vawobe.manager.CommandManager;
import vawobe.manager.MidiManager;
import vawobe.manager.SelectionManager;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;

import java.util.HashMap;

import static vawobe.Main.mainColor;
import static vawobe.Main.mainPane;

public class InstrumentSelector extends ComboBox<Instruments> {
    public InstrumentSelector() {
        super();
        setBackground(new Background(new BackgroundFill(mainColor,new CornerRadii(5), null)));

        getItems().addAll(Instruments.values());
        setValue(Instruments.values()[1]);
        setTooltip(new Tooltip(getValue().getName()));

        setCellFactory(_ -> new ListCell<>() {
            @Override
            protected void updateItem(Instruments item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                }
                else {
                    setText(item.getName());
                    if(item.getNum() == -1) {
                        setDisable(true);
                        setTextFill(Color.WHITE);
                        SVGPath icon = new SVGPath();
                        icon.setContent(item.getSvg());
                        icon.setFill(Color.WHITE);
                        icon.setStroke(Color.WHITE);
                        icon.setStrokeWidth(1.0);
                        setGraphic(icon);
                        setBackground(new Background(new BackgroundFill(Color.SLATEGRAY, null, null)));
                    } else {
                        setTextFill(Color.DARKGRAY);
                        hoverProperty().addListener((_,_,newValue) -> {
                            setBackground(new Background(new BackgroundFill(newValue ? Color.ORANGE : Color.TRANSPARENT, null, null)));
                            setTextFill(newValue ? Color.WHITE : Color.DARKGRAY);
                        });
                        setDisable(false);
                        if (item.getColor() != null) {
                            Circle colorCircle = new Circle(5, item.getColor());
                            colorCircle.setStroke(Color.DARKGRAY);
                            setGraphic(colorCircle);
                        } else {
                            setGraphic(null);
                        }
                        setBackground(null);
                    }
                }
            }
        });

        setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Instruments item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else {
                    setText(item.getName());
                    setTextFill(Color.WHITE);
                }
            }
        });

        valueProperty().addListener((_, oldValue, newValue) -> {
            MidiManager.getInstance().changeDemoChannel(newValue);
            if (newValue == Instruments.DRUMS || oldValue == Instruments.DRUMS) {
                mainPane.getPianoGridPane().changeKeyBox();
            }
            HashMap<NoteView, Instruments> oldInstruments = new HashMap<>();
            for(NoteView note : SelectionManager.getInstance().getSelectedNotes()) {
                oldInstruments.put(note, note.getViewModel().getInstrumentProperty().get());
            }
            CommandManager.getInstance().executeCommand(new ChangeInstrumentCommand(oldInstruments, getValue()));
            setTooltip(new Tooltip(newValue.getName()));
        });
    }

    public int getCurrentInstrumentsChannel() {
        return MidiManager.getInstrumentToChannel().getOrDefault(getValue(), -1);
    }

    public int addCurrentInstrument() {
        return MidiManager.getInstance().addInstrument(getValue());
    }
}
