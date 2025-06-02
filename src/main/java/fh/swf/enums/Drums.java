package fh.swf.enums;

public enum Drums {
    ACOUSTIC_BASS_DRUM("Acoustic Bass Drum", 35),
    BASS_DRUM_1("Bass Drum 1", 36),
    SIDE_STICK("Side Stick", 37),
    ACOUSTIC_SNARE("Acoustic Snare", 38),
    HAND_CLAP("Hand Clap", 39),
    ELECTRIC_SNARE("Electric Snare", 40),
    LOW_FLOOR_TOM("Low Floor Tom", 41),
    CLOSED_HI_HAT("Closed Hi-Hat", 42),
    HIGH_FLOOR_TOM("High Floor Tom", 43),
    PEDAL_HI_HAT("Pedal Hi-Hat", 44),
    LOW_TOM("Low Tom", 45),
    OPEN_HI_HAT("Open Hi-Hat", 46),
    LOW_MID_TOM("Low-Mid Tom", 47),
    HI_MID_TOM("Hi-Mid Tom", 48),
    CRASH_CYMBAL_1("Crash Cymbal 1", 49),
    HIGH_TOM("High Tom", 50),
    RIDE_CYMBAL_1("Ride Cymbal 1", 51),
    CHINESE_CYMBAL("Chinese Cymbal", 52),
    RIDE_BELL("Ride Bell", 53),
    TAMBOURINE("Tambourine", 54),
    SPLASH_CYMBAL("Splash Cymbal", 55),
    COWBELL("Cowbell", 56),
    CRASH_CYMBAL_2("Crash Cymbal 2", 57),
    VIBRASLAP("Vibraslap", 58),
    RIDE_CYMBAL_2("Ride Cymbal 2", 59),
    HI_BONGO("Hi Bongo", 60),
    LOW_BONGO("Low Bongo", 61),
    MUTE_HI_CONGA("Mute Hi Conga", 62),
    OPEN_HI_CONGA("Open Hi Conga", 63),
    LOW_CONGA("Low Conga", 64),
    HIGH_TIMBALE("High Timbale", 65),
    LOW_TIMBALE("Low Timbale", 66),
    HIGH_AGOGO("High Agogo", 67),
    LOW_AGOGO("Low Agogo", 68),
    CABASA("Cabasa", 69),
    MARACAS("Maracas", 70),
    SHORT_WHISTLE("Short Whistle", 71),
    LONG_WHISTLE("Long Whistle", 72),
    SHORT_GUIRO("Short Guiro", 73),
    LONG_GUIRO("Long Guiro", 74),
    CLAVES("Claves", 75),
    HI_WOOD_BLOCK("Hi Wood Block", 76),
    LOW_WOOD_BLOCK("Low Wood Block", 77),
    MUTE_CUICA("Mute Cuica", 78),
    OPEN_CUICA("Open Cuica", 79),
    MUTE_TRIANGLE("Mute Triangle", 80),
    OPEN_TRIANGLE("Open Triangle", 81);

    private final String name;
    private final int num;

    Drums(String name, int num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }
    public int getNum() {
        return num;
    }
}
