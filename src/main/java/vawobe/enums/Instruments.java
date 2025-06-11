package vawobe.enums;

import javafx.scene.paint.Color;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

import static javafx.scene.paint.Color.*;

@Getter
public enum Instruments {
    TITLE_1("Piano", "M0 0H14V3H0ZM0 3v7H4V9H1V3H3V6H4v4H5V6H6V3H8V6H9V9H5v1h5V6h1V3h2V9H10v1h4V3"),
    ACOUSTIC_GRAND_PIANO("Acoustic Grand Piano", 0, RED),
    BRIGHT_ACOUSTIC_PIANO("Bright Acoustic Piano", 1, GREEN),
    ELECTRIC_GRAND_PIANO("Electric Grand Piano", 2, BLUE),
    HONKY_TONK_PIANO("Honky-tonk Piano", 3, YELLOW),
    ELECTRIC_PIANO_1("Electric Piano 1", 4, CYAN),
    ELECTRIC_PIANO_2("Electric Piano 2", 5, MAGENTA),
    HARPSICHORD("Harpsichord", 6, ORANGE),
    CLAVI("Clavi", 7, PINK),
    TITLE_2("Chromatic Percussion", "M3 2H4V12H3V2ZM6 3H7V11H6V3ZM9 4H10V10H9V4ZM12 5H13V9H12V5Z"),
    CELESTA("Celesta", 8, BROWN),
    GLOCKENSPIEL("Glockenspiel", 9, LIME),
    MUSIC_BOX("Music Box", 10, DARKBLUE),
    VIBRAPHONE("Vibraphone", 11, DARKGREEN),
    MARIMBA("Marimba", 12, DARKRED),
    XYLOPHONE("Xylophone", 13, DARKORANGE),
    TUBULAR_BELLS("Tubular Bells", 14, DARKVIOLET),
    DULCIMER("Dulcimer", 15, MAROON),
    TITLE_3("Organ", "M3 2H4V12H3V2ZM6 4H7v7H6ZM9 6h1v4H9ZM1 4H0v7H1ZM-2 6H-3v4h1Z"),
    DRAWBAR_ORGAN("Drawbar Organ", 16, TEAL),
    PERCUSSIVE_ORGAN("Percussive Organ", 17, NAVY),
    ROCK_ORGAN("Rock Organ", 18, OLIVE),
    CHURCH_ORGAN("Church Organ", 19, SALMON),
    REED_ORGAN("Reed Organ", 20, GOLD),
    ACCORDION("Accordion", 21, SILVER),
    HARMONICA("Harmonica", 22, CORAL),
    TANGO_ACCORDION("Tango Accordion", 23, INDIGO),
    TITLE_4("Guitar", "M9.5 5.5h1L11 6c1 0 1-1.5 0-1.5l-.5.5h-1ZM9.5 8h1l.5.5c1 0 1-1.5 0-1.5l-.5.5h-1Zm0-5h1l.5.5c1 0 1-1.5 0-1.5l-.5.5h-1Zm-6 5h-1L2 8.5C1 8.5 1 7 2 7l.5.5h1Zm0-2.5h-1L2 6C1 6 1 4.5 2 4.5l.5.5h1ZM3.5 3h-1L2 3.5C1 3.5 1 2 2 2l.5.5h1Zm0 8 3 1 3-1L9 .5 6.5 1 4 .5Z"),
    ACOUSTIC_GUITAR_NYLON("Acoustic Guitar (nylon)", 24, PERU),
    ACOUSTIC_GUITAR_STEEL("Acoustic Guitar (steel)", 25, TURQUOISE),
    ELECTRIC_GUITAR_JAZZ("Electric Guitar (jazz)", 26, LAVENDER),
    ELECTRIC_GUITAR_CLEAN("Electric Guitar (clean)", 27, SIENNA),
    ELECTRIC_GUITAR_MUTED("Electric Guitar (muted)", 28, KHAKI),
    OVERDRIVEN_GUITAR("Overdriven Guitar", 29, PLUM),
    DISTORTION_GUITAR("Distortion Guitar", 30, ORCHID),
    GUITAR_HARMONICS("Guitar Harmonics", 31, IVORY),
    TITLE_5("Bass", "m5.5 3.5-.25.25c-.75 0-.75-1 0-1L5.5 3h.75v.5ZM5 5.25l-.25.25c-.75 0-.75-1 0-1l.25.25h.75v.5ZM4.25 7 4 7.25c-.75 0-.75-1 0-1l.25.25H5V7Zm-.5 1.75L3.5 9c-.75 0-.75-1 0-1l.25.25H4.5v.5Zm1.5 3.5c0-2.25-.25-2-1-2.5L6.75 3c.5-.75 1-2 2.25-2 1.5.25 2 2-.25 3.5C9.5 5.75 10 7.25 10.25 9 8.5 9 7.5 10.75 7.5 12.25H5.25Z"),
    ACOUSTIC_BASS("Acoustic Bass", 32, TAN),
    ELECTRIC_BASS_FINGER("Electric Bass (finger)", 33, SKYBLUE),
    ELECTRIC_BASS_PICK("Electric Bass (pick)", 34, CRIMSON),
    FRETLESS_BASS("Fretless Bass", 35, SLATEGRAY),
    SLAP_BASS_1("Slap Bass 1", 36, SEAGREEN),
    SLAP_BASS_2("Slap Bass 2", 37, MIDNIGHTBLUE),
    SYNTH_BASS_1("Synth Bass 1", 38, DARKCYAN),
    SYNTH_BASS_2("Synth Bass 2", 39, DARKKHAKI),
    TITLE_6("Strings", ""),
    VIOLIN("Violin", 40, DARKOLIVEGREEN),
    VIOLA("Viola", 41, CHOCOLATE),
    CELLO("Cello", 42, STEELBLUE),
    CONTRABASS("Contrabass", 43, TOMATO),
    TREMOLO_STRINGS("Tremolo Strings", 44, PALEGOLDENROD),
    PIZZICATO_STRINGS("Pizzicato Strings", 45, DARKSALMON),
    ORCHESTRAL_HARP("Orchestral Harp", 46, LIGHTSEAGREEN),
    TIMPANI("Timpani", 47, SANDYBROWN),
    TITLE_7("Ensemble", ""),
    STRING_ENSEMBLE_1("String Ensemble 1", 48, LIGHTCORAL),
    STRING_ENSEMBLE_2("String Ensemble 2", 49, ROSYBROWN),
    SYNTHSTRINGS_1("SynthStrings 1", 50, DARKSLATEBLUE),
    SYNTHSTRINGS_2("SynthStrings 2", 51, PEACHPUFF),
    CHOIR_AAHS("Choir Aahs", 52, MISTYROSE),
    VOICE_OOHS("Voice Ooohs", 53, THISTLE),
    SYNTH_VOICE("Synth Voice", 54, GAINSBORO),
    ORCHESTRA_HIT("Orchestra Hit", 55, BEIGE),
    TITLE_8("Brass", ""),
    TRUMPET("Trumpet", 56, LEMONCHIFFON),
    TROMBONE("Trombone", 57, ANTIQUEWHITE),
    TUBA("Tuba", 58, LINEN),
    MUTED_TRUMPET("Muted Trumpet", 59, HONEYDEW),
    FRENCH_HORN("French Horn", 60, ALICEBLUE),
    BRASS_SECTION("Brass Section", 61, MINTCREAM),
    SYNTHBRASS_1("SynthBrass 1", 62, AZURE),
    SYNTHBRASS_2("SynthBrass 2", 63, SNOW),
    TITLE_9("Reed", ""),
    SOPRANO_SAX("Soprano Sax", 64, WHITESMOKE),
    ALTO_SAX("Alto Sax", 65, OLDLACE),
    TENOR_SAX("Tenor Sax", 66, FLORALWHITE),
    BARITONE_SAX("Baritone Sax", 67, GHOSTWHITE),
    OBOE("Oboe", 68, SEASHELL),
    ENGLISH_HORN("English Horn", 69, IVORY),
    BASSOON("Bassoon", 70, CORNSILK),
    CLARINET("Clarinet", 71, LAVENDERBLUSH),
    TITLE_10("Pipe", ""),
    PICCOLO("Piccolo", 72, PAPAYAWHIP),
    FLUTE("Flute", 73, BLANCHEDALMOND),
    RECORDER("Recorder", 74, BISQUE),
    PAN_FLUTE("Pan Flute", 75, WHEAT),
    BLOWN_BOTTLE("Blown Bottle", 76, MOCCASIN),
    SHAKUHACHI("Shakuhachi", 77, NAVAJOWHITE),
    WHISTLE("Whistle", 78, BURLYWOOD),
    OCARINA("Ocarina", 79, TAN),
    TITLE_11("Synth Lead", ""),
    LEAD_1_SQUARE("Lead 1 (square)", 80, LIGHTSKYBLUE),
    LEAD_2_SAWTOOTH("Lead 2 (sawtooth)", 81, MEDIUMAQUAMARINE),
    LEAD_3_CALLIOPE("Lead 3 (calliope)", 82, CORNFLOWERBLUE),
    LEAD_4_CHIFF("Lead 4 (chiff)", 83, MEDIUMORCHID),
    LEAD_5_CHARANG("Lead 5 (charang)", 84, DARKSEAGREEN),
    LEAD_6_VOICE("Lead 6 (voice)", 85, MEDIUMTURQUOISE),
    LEAD_7_FIFTHS("Lead 7 (fifths)", 86, LIGHTSTEELBLUE),
    LEAD_8_BASS_LEAD("Lead 8 (bass + lead)", 87, LIGHTSLATEGRAY),
    TITLE_12("Synth Pad", ""),
    PAD_1_NEW_AGE("Pad 1 (new age)", 88, LIGHTPINK),
    PAD_2_WARM("Pad 2 (warm)", 89, Color.valueOf("#FFB347")),       // warm orange
    PAD_3_POLYSYNTH("Pad 3 (polysynth)", 90, Color.valueOf("#B19CD9")), // weiches Lila
    PAD_4_CHOIR("Pad 4 (choir)", 91, Color.valueOf("#D8BFD8")),     // helle Chorfarbe
    PAD_5_BOWED("Pad 5 (bowed)", 92, Color.valueOf("#A9A9F5")),     // kaltes Blau
    PAD_6_METALLIC("Pad 6 (metallic)", 93, Color.valueOf("#C0C0C0")), // silber/metallisch
    PAD_7_HALO("Pad 7 (halo)", 94, Color.valueOf("#E0FFFF")),       // himmlisch hellblau
    PAD_8_SWEEP("Pad 8 (sweep)", 95, Color.valueOf("#98FB98")),     // weiches Mintgrün
    TITLE_13("Synth Effects",""),
    FX_1_RAIN("FX 1 (rain)", 96, Color.valueOf("#4682B4")),         // stahlblau
    FX_2_SOUNDTRACK("FX 2 (soundtrack)", 97, Color.valueOf("#708090")), // filmisch grau
    FX_3_CRYSTAL("FX 3 (crystal)", 98, Color.valueOf("#AFEEEE")),   // kristallhellblau
    FX_4_ATMOSPHERE("FX 4 (atmosphere)", 99, Color.valueOf("#87CEEB")), // atmosphärisch blau
    FX_5_BRIGHTNESS("FX 5 (brightness)", 100, Color.valueOf("#FFFF66")), // leuchtend gelb
    FX_6_GOBLINS("FX 6 (goblins)", 101, Color.valueOf("#556B2F")),  // dunkelgrün
    FX_7_ECHOES("FX 7 (echoes)", 102, Color.valueOf("#DCDCDC")),    // soft grau
    FX_8_SCI_FI("FX 8 (sci-fi)", 103, Color.valueOf("#00CED1")),    // neon türkis
    TITLE_14("Ethnic",""),
    SITAR("Sitar", 104, Color.valueOf("#DAA520")),                  // goldbraun
    BANJO("Banjo", 105, Color.valueOf("#DEB887")),                  // banjoholz
    SHAMISEN("Shamisen", 106, Color.valueOf("#CD853F")),            // rustikal braun
    KOTO("Koto", 107, Color.valueOf("#F4A460")),                    // sandfarben
    KALIMBA("Kalimba", 108, Color.valueOf("#8B4513")),              // dunkelholz
    BAGPIPE("Bagpipe", 109, Color.valueOf("#006400")),              // schottisch grün
    FIDDLE("Fiddle", 110, Color.valueOf("#A0522D")),                // geigenbraun
    SHANAI("Shanai", 111, Color.valueOf("#BC8F8F")),                // rotbraun
    TITLE_15("Percussive",""),
    TINKLE_BELL("Tinkle Bell", 112, Color.valueOf("#FFFACD")),      // zartgelb
    AGOGO("Agogo", 113, Color.valueOf("#C0C0C0")),                  // metallisch silber
    STEEL_DRUM("Steel Drums", 114, Color.valueOf("#4682B4")),       // stahlblau
    WOODBLOCK("Woodblock", 115, Color.valueOf("#A0522D")),          // holzfarben
    TAIKO_DRUM("Taiko Drum", 116, Color.valueOf("#800000")),        // tiefrot
    MELODIC_TOM("Melodic Tom", 117, Color.valueOf("#8B0000")),      // dunkler Trommelsound
    SYNTH_DRUM("Synth Drum", 118, Color.valueOf("#FF69B4")),        // neon pink
    REVERSE_CYMBAL("Reverse Cymbal", 119, Color.valueOf("#D3D3D3")),// silbrig
    GUITAR_FRET_NOISE("Guitar Fret Noise", 120, Color.valueOf("#A9A9A9")), // neutral grau
    BREATH_NOISE("Breath Noise", 121, Color.valueOf("#F5F5DC")),    // fast weiß
    SEASHORE("Seashore", 122, Color.valueOf("#00BFFF")),            // meerblau
    BIRD_TWEET("Bird Tweet", 123, Color.valueOf("#FFD700")),        // kanariengelb
    TELEPHONE_RING("Telephone Ring", 124, Color.valueOf("#FF4500")),// warnrot
    HELICOPTER("Helicopter", 125, Color.valueOf("#2F4F4F")),        // militaristisch dunkel
    APPLAUSE("Applause", 126, Color.valueOf("#FFB6C1")),            // sanft rosa
    GUNSHOT("Gunshot", 127, Color.valueOf("#8B0000")),              // blutrot
    TITLE_17("Drums", ""),
    DRUMS("Drums", 200, Color.valueOf("#000000"));

    private final String name;
    private final int num;
    private String svg;
    private final Color color;

    Instruments(String name, int num, Color color) {
        this.name = name;
        this.num = num;
        this.color = color;
    }

    Instruments(String name, String svg) {
        this(name, -1, null);
        this.svg = svg;
    }

    public static Instruments getInstrumentByNum(int number) {
        return Arrays.stream(values()).filter(instruments -> instruments.getNum() == number).findFirst().orElse(ACOUSTIC_GRAND_PIANO);
    }
    public static Instruments getInstrumentByName(String name) {
        return Arrays.stream(values()).filter(instruments -> Objects.equals(instruments.getName(), name)).findFirst().orElse(ACOUSTIC_GRAND_PIANO);
    }
}
