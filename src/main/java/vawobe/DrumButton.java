package vawobe;

import vawobe.enums.Drums;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import vawobe.model.manager.MidiManager;

public class DrumButton extends KeyButton{
    private final Drums drum;

    public DrumButton(Drums drum, Color color, int row) {
        super(null, row);
        this.drum = drum;
        setText(drum.getName());

        normalBackground = new Background(new BackgroundFill(color, null, null));
        hoverBackground = new Background(new BackgroundFill(color == Color.BLACK ? Color.DARKGRAY : Color.LIGHTGRAY, null, null));
        
        setBackground(new Background(new BackgroundFill(color, null, null)));
        if(color == Color.BLACK) setTextFill(Color.WHITE);
    }
    @Override
    public void playTone() {
        if(!isPlaying) {
            int midiNote = drum.getNum();
            MidiManager.getInstance().playDemoDrumTone(midiNote);
            isPlaying = true;
        }
    }

    @Override
    public void stopTone() {
        if(isPlaying) {
            int midiNote = drum.getNum();
            MidiManager.getInstance().stopDemoDrumTone(midiNote);
            isPlaying = false;
        }
    }
}
