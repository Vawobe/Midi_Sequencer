package vawobe;

public class ToneToMidiConverter {
    public static int convertToMidi(String note) {
        String toneName = note.substring(0, note.length() - 1);
        int octave = Integer.parseInt(note.substring(note.length() - 1));

        int base = switch (toneName) {
            case "C" -> 0;
            case "C#" -> 1;
            case "D" -> 2;
            case "D#" -> 3;
            case "E" -> 4;
            case "F" -> 5;
            case "F#" -> 6;
            case "G" -> 7;
            case "G#" -> 8;
            case "A" -> 9;
            case "A#" -> 10;
            case "B" -> 11;
            default -> throw new IllegalArgumentException("Unbekannter Ton: " + toneName);
        };

        return 12 * (octave + 1) + base; // MIDI-Standard: C4 = 60
    }

}
