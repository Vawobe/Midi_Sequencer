package fh.swf;

import fh.swf.enums.Instruments;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.Tooltip;
import lombok.Getter;

import java.util.*;

import static fh.swf.Main.mainPane;

public class InstrumentSelector extends ComboBox<Instruments> {
    @Getter private final Map<Instruments, Integer> instrumentToChannel = new HashMap<>();
    private final ArrayList<Integer> freeChannels = new ArrayList<>();

    public int getNextFreeChannel() {
        Collections.sort(freeChannels);
        int nextFreeChannel = freeChannels.getFirst();
        freeChannels.removeFirst();
        return nextFreeChannel;
    }

    public int getCurrentChannel() { return instrumentToChannel.getOrDefault(getValue(),0); }

    public InstrumentSelector() {
        super();
        freeChannels.add(1);
        freeChannels.add(2);
        freeChannels.add(3);
        freeChannels.add(4);
        freeChannels.add(5);
        freeChannels.add(6);
        freeChannels.add(7);
        freeChannels.add(8);
        freeChannels.add(9);
        freeChannels.add(11);
        freeChannels.add(12);
        freeChannels.add(13);
        freeChannels.add(14);
        freeChannels.add(15);

        getItems().addAll(Instruments.values());
        setValue(Instruments.values()[1]);
        setTooltip(new Tooltip(getValue().getName()));


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
            if(!instrumentToChannel.containsKey(newValue)) {
                MidiManager.getInstance().changeDemoChannel(newValue);
            }
            if (newValue == Instruments.DRUMS || oldValue == Instruments.DRUMS) {
                mainPane.getPianoPane().changeKeyBox();
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
}
