package vawobe.menubar.instrument;

import vawobe.NoteView;
import vawobe.enums.Instruments;
import vawobe.model.manager.MidiManager;
import vawobe.render.NoteRenderer;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.SVGPath;
import lombok.Getter;

import java.util.*;

import static vawobe.Main.mainColor;
import static vawobe.Main.mainPane;

public class InstrumentSelector extends ComboBox<Instruments> {
    @Getter private final Map<Instruments, Integer> instrumentToChannel = new HashMap<>();
    private final ArrayList<Integer> freeChannels = new ArrayList<>();

    public InstrumentSelector() {
        super();
        setBackground(new Background(new BackgroundFill(mainColor,new CornerRadii(5), null)));

        for(int i = 1; i <= 15; i++)
            if(i != 10) freeChannels.add(i);

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
            if(!instrumentToChannel.containsKey(newValue)) {
                MidiManager.getInstance().changeDemoChannel(newValue);
            }
            if (newValue == Instruments.DRUMS || oldValue == Instruments.DRUMS) {
                mainPane.getPianoGridPane().changeKeyBox();
            }
            for(Node node : NoteRenderer.getInstance().getChildren()) {
                if(node instanceof NoteView note) {
                    if(note.getSelectedProperty().get()) {
                        MidiManager.getInstance().stopNote(note.getViewModel().getNote());
                        note.getViewModel().getInstrumentProperty().set(newValue);
                        int newChannel = getCurrentInstrumentsChannel();
                        if (newChannel == -1) {
                            newChannel = getNextFreeChannel();
                            addCurrentInstrument(newChannel);
                        }
                        note.getViewModel().getChannelProperty().set(newChannel);

                        note.getViewModel().updateNote();
                    }
                }
            }
            setTooltip(new Tooltip(newValue.getName()));
        });
    }

    public int getCurrentInstrumentsChannel() {
        return instrumentToChannel.getOrDefault(getValue(), -1);
    }

    public void addCurrentInstrument(int channel) {
        instrumentToChannel.put(getValue(), channel);
        MidiManager.getInstance().changeInstrument(getValue(), channel);
    }

    public void removeChannel(int channel) {
        Instruments instrument = instrumentToChannel.entrySet().stream().
                filter(entry -> entry.getValue() == channel).findFirst().orElseThrow().getKey();
        instrumentToChannel.remove(instrument);
        freeChannels.add(channel);
    }



    public int getNextFreeChannel() {
        Collections.sort(freeChannels);
        int nextFreeChannel = freeChannels.getFirst();
        freeChannels.removeFirst();
        return nextFreeChannel;
    }
}
