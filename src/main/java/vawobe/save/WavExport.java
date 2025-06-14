package vawobe.save;

import javax.sound.midi.*;
import javax.sound.sampled.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.sun.media.sound.AudioSynthesizer;

public class WavExport {
    public static void exportMIDIToWav(Sequence sequence, File outputFile) throws Exception {
        AudioSynthesizer synth = findAudioSynthesizer();
        if (synth == null) {
            throw new IllegalStateException("AudioSynthesizer not found");
        }

        AudioFormat format = new AudioFormat(44100, 16, 2, true, false);
        AudioInputStream stream = synth.openStream(format, null);
        double totalSeconds = send(sequence, synth.getReceiver());
        long frameLength = (long)(format.getFrameRate() * totalSeconds);
        AudioInputStream limited = new AudioInputStream(stream, format, frameLength);

        AudioSystem.write(limited, AudioFileFormat.Type.WAVE, outputFile);

        stream.close();
        synth.close();
    }

    private static AudioSynthesizer findAudioSynthesizer() throws MidiUnavailableException {
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (MidiDevice.Info info : infos) {
            MidiDevice dev = MidiSystem.getMidiDevice(info);
            if (dev instanceof AudioSynthesizer) {
                return (AudioSynthesizer) dev;
            }
        }
        return null;
    }

    private static double send(Sequence seq, Receiver recv) {
        Track[] tracks = seq.getTracks();
        int resolution = seq.getResolution();

        float mpq = 500000f;

        List<MidiEvent> events = new ArrayList<>();
        for (Track track : tracks) {
            for (int i = 0; i < track.size(); i++) {
                events.add(track.get(i));
            }
        }

        events.sort(Comparator.comparingLong(MidiEvent::getTick));

        long currentTick = 0;
        long currentTimeUs = 0;

        for (MidiEvent event : events) {
            long tick = event.getTick();
            MidiMessage msg = event.getMessage();

            if (msg instanceof MetaMessage mm && mm.getType() == 0x51) {
                byte[] data = mm.getData();
                mpq = ((data[0] & 0xFF) << 16) | ((data[1] & 0xFF) << 8) | (data[2] & 0xFF);
            }

            long deltaTicks = tick - currentTick;
            currentTick = tick;

            long deltaTimeUs = (long)((mpq * deltaTicks) / resolution);
            currentTimeUs += deltaTimeUs;

            if (!(msg instanceof MetaMessage || msg instanceof SysexMessage)) {
                recv.send(msg, currentTimeUs);
            }
        }

        return currentTimeUs / 1_000_000.0;
    }
}
