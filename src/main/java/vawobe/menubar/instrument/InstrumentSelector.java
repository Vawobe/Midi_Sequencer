package vawobe.menubar.instrument;

import vawobe.NoteView;
import vawobe.enums.Instruments;
import vawobe.model.manager.MidiManager;
import vawobe.render.NoteRenderer;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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
            HashSet<Instruments> oldInstruments = new HashSet<>();
            for(NoteView note : NoteRenderer.getInstance().getSelectedNotes()) {
                oldInstruments.add(note.getViewModel().getInstrumentProperty().get());

                MidiManager.getInstance().stopNote(note.getViewModel().getNote());
                int newChannel = getCurrentInstrumentsChannel();
                if (newChannel == -1) {
                    newChannel = addCurrentInstrument();
                }
                note.getViewModel().getInstrumentProperty().set(newValue);
                note.getViewModel().getChannelProperty().set(newChannel);

                note.getViewModel().updateNote();
            }
            oldInstruments.forEach(instrument -> MidiManager.getInstance().removeInstrumentIfNecessary(instrument));
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
