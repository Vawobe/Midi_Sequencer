package vawobe;

public class ToneToMidiConverter {
    public static int convertToMidi(String note) {
        String toneName = note.substring(0, note.length() - 1);
        int octave = Integer.parseInt(note.substring(note.length() - 1));

        int base;
        switch (toneName) {
            case "C": base = 0; break;
            case "C#": base = 1; break;
            case "D": base = 2; break;
            case "D#": base = 3; break;
            case "E": base = 4; break;
            case "F": base = 5; break;
            case "F#": base = 6; break;
            case "G": base = 7; break;
            case "G#": base = 8; break;
            case "A": base = 9; break;
            case "A#": base = 10; break;
            case "B": base = 11; break;
            default: throw new IllegalArgumentException("Unbekannter Ton: " + toneName);
        }

        return 12 * (octave + 1) + base - 12;
    }
}
