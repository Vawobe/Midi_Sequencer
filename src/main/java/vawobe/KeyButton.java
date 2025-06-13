package vawobe;

import vawobe.manager.MidiManager;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Getter;

import static vawobe.KeyBox.WIDTH;
import static vawobe.Main.mainPane;
import static vawobe.ToneToMidiConverter.convertToMidi;

public class KeyButton extends Button {
    protected String key;
    protected boolean isPlaying = false;
    @Getter protected final int row;
    protected Background normalBackground;
    protected Background hoverBackground;

    public KeyButton(String key, int row) {
        super(key);
        setFocusTraversable(false);
        this.row = row;
        setAlignment(Pos.CENTER_RIGHT);

        setFont(Font.font(12));
        setPrefWidth(350);
        setPrefHeight(25);
        setLayoutX(WIDTH-getPrefWidth());

        if(key != null) {
            this.key = key;
            normalBackground = new Background(new BackgroundFill(key.contains("#") ? Color.BLACK : Color.WHITE, null, null));
            hoverBackground = new Background(new BackgroundFill(key.contains("#") ? Color.DARKGRAY : Color.LIGHTGRAY, null, null));
            setBackground(normalBackground);
            setTextFill(key.contains("#") ? Color.WHITE : Color.BLACK);
        }
        hoverProperty().addListener((_, _, newValue) -> setBackground(newValue ? hoverBackground : normalBackground));

        setOnMousePressed(this::onMousePressedEvent);
        setOnDragDetected(this::onDragDetectedEvent);
        setOnMouseReleased(this::onMouseReleasedEvent);
        setOnMouseDragEntered(this::onMouseDragEnteredEvent);
        setOnMouseDragExited(this::onMouseDragExitedEvent);
    }

    public void playTone() {
        if(!isPlaying) {
            setBackground(new Background(new BackgroundFill(mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().getValue().getColor(), null, null)));
            if(key != null) {
                int midiNote = convertToMidi(key);
                MidiManager.getInstance().changeDemoChannel(mainPane.getMenuBar().getInstrumentBox().getInstrumentSelector().getValue());
                MidiManager.getInstance().playDemoTone(midiNote);
                isPlaying = true;
            }
        }
    }

    public void stopTone() {
        if(isPlaying) {
            if(isHover()) setBackground(hoverBackground);
            else setBackground(normalBackground);
            if(key != null) {
                int midiNote = convertToMidi(key);
                MidiManager.getInstance().stopDemoTone(midiNote);
                isPlaying = false;
            }
        }
    }

    public void zoom() {
        setFont(Font.font(12*PianoGridPane.zoomY.get()));
        setPrefHeight(25*PianoGridPane.zoomY.get());
        setLayoutY(row*25*PianoGridPane.zoomY.get());
    }

    private void onMousePressedEvent(MouseEvent event) {
        playTone();
        event.consume();
    }
    private void onDragDetectedEvent(MouseEvent event) {
        startFullDrag();
        event.consume();
    }
    private void onMouseReleasedEvent(MouseEvent event) {
        stopTone();
        event.consume();
    }
    private void onMouseDragEnteredEvent(MouseEvent event) {
        playTone();
        event.consume();
    }
    private void onMouseDragExitedEvent(MouseEvent event) {
        stopTone();
        event.consume();
    }
}
