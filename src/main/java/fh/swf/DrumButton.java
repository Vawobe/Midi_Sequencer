package fh.swf;

import fh.swf.enums.Drums;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public class DrumButton extends KeyButton{
    private final Drums drum;
    public DrumButton(Drums drum, Color color, int row) {
        super(null, row);
        this.drum = drum;
        setText(drum.getName());
        
        setBackground(new Background(new BackgroundFill(color, null, null)));
        if(color == Color.BLACK) setTextFill(Color.WHITE);
    }
    @Override
    public void playTone() {
        if(!isPlaying) {
            int midiNote = drum.getNum();
            MidiManager.getInstance().playNote(midiNote);
            isPlaying = true;
        }
    }

    public void stopTone() {
        if(isPlaying) {
            int midiNote = drum.getNum();
            MidiManager.getInstance().stopNote(midiNote);
            isPlaying = false;
        }
    }
}
