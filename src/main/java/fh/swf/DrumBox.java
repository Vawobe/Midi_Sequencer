package fh.swf;

import fh.swf.enums.Drums;
import javafx.scene.paint.Color;

public class DrumBox extends KeyBox{
    @Override
    protected void drawToneNames() {
        int row = 0;
        for(Drums drum : Drums.values()) {
            int noteInOctave = row % 12;
            boolean isBlack = switch (noteInOctave) {
                case 1, 3, 5, 8, 10 -> true;
                default -> false;
            };

            DrumButton drumButton = new DrumButton(drum, isBlack ? Color.BLACK : Color.WHITE, row);
            getChildren().add(drumButton);
            drumButton.setLayoutY(row * 25 * PianoPane.zoomX);
            row++;
        }
    }
}
