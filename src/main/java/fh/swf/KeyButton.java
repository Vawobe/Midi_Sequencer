package fh.swf;

import fh.swf.model.manager.MidiManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Getter;

import static fh.swf.KeyBox.WIDTH;
import static fh.swf.Main.mainPane;
import static fh.swf.ToneToMidiConverter.convertToMidi;

public class KeyButton extends Button {
    protected String key;
    protected boolean isPlaying = false;
    @Getter protected final int row;

    public KeyButton(String key, int row) {
        super(key);
        this.row = row;
        setAlignment(Pos.CENTER_RIGHT);

        setFont(Font.font(12));
        setPrefWidth(350);
        setPrefHeight(25);
        setLayoutX(WIDTH-getPrefWidth());

        if(key != null) {
            this.key = key;
            setBackground(new Background(new BackgroundFill(key.contains("#") ? Color.BLACK : Color.WHITE, null, null)));
            setTextFill(key.contains("#") ? Color.WHITE : Color.BLACK);
        }
        setOnMousePressed(event -> {
            playTone();
            event.consume();
        });
        setOnDragDetected(event -> {
            startFullDrag();
            event.consume();
        });

        setOnMouseReleased(event -> {
            stopTone();
            event.consume();
        });

        setOnMouseDragEntered(event -> {
            playTone();
            event.consume();
        });

        setOnMouseDragExited(event -> {
            stopTone();
            event.consume();
        });
    }

    public void playTone() {
        if(!isPlaying) {
            int midiNote = convertToMidi(key);
            MidiManager.getInstance().changeDemoChannel(mainPane.getMenuBar().getInstrumentSelector().getValue());
            MidiManager.getInstance().playDemoTone(midiNote);
            isPlaying = true;
        }
    }

    public void stopTone() {
        if(isPlaying) {
            int midiNote = convertToMidi(key);
            MidiManager.getInstance().stopDemoTone(midiNote);
            isPlaying = false;
        }
    }

    public void zoom() {

        setFont(Font.font(12*PianoGridPane.zoomY));
        setPrefHeight(25*PianoGridPane.zoomY);
        setLayoutY(row*25*PianoGridPane.zoomY);
//        setFont(Font.font(12*PianoGridPane.zoomY));
//        setPrefHeight(25*PianoGridPane.zoomY);
//        setLayoutY(row*25*PianoGridPane.zoomY);
    }
}
