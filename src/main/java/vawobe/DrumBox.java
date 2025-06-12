package vawobe;

import vawobe.enums.Drums;
import javafx.scene.paint.Color;

import static vawobe.model.manager.NoteManager.OCTAVES;
import static vawobe.model.manager.NoteManager.TONES;

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

            drumButton.setLayoutY(row * 25 * PianoGridPane.zoomX.get());
            row++;
        }

        int buttonAmount = OCTAVES.length * TONES.length - getChildren().size();

        for(int i = 0; i < buttonAmount; i++) {
            getChildren().add(new FillerKeyButton(getChildren().size()));
        }
    }
}
