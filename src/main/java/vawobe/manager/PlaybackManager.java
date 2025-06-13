package vawobe.manager;

import vawobe.Note;
import vawobe.PianoGrid;
import vawobe.PianoGridPane;
import vawobe.render.GridRenderer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import lombok.Getter;

import java.util.ArrayList;

import static vawobe.render.GridRenderer.CELL_WIDTH;

public class PlaybackManager {
    private static PlaybackManager instance;


    private Timeline timeline;
    private double pausedBeats;
    @Getter private final SimpleBooleanProperty isPlayingProperty;
    @Getter private final SimpleIntegerProperty bpmProperty;

    public boolean isPlaying() { return isPlayingProperty.get(); }

    public static PlaybackManager getInstance() {
        if(instance == null)
            instance = new PlaybackManager();
        return instance;
    }

    private PlaybackManager() {
        isPlayingProperty = new SimpleBooleanProperty(false);
        bpmProperty = new SimpleIntegerProperty(120);
        pausedBeats = 0;
    }

    public void startPlaybackAtSeconds(double timeInSeconds) {
        stopPlayback();
        pausedBeats = timeInSeconds * (bpmProperty.get() / 60.0);
        startPlayback();
    }


    public void startPlayback() {
        if(!isPlaying()) {
            isPlayingProperty.set(true);

            PianoGrid.getPlayhead().setVisible(true);
            updateNotes();
        }
    }

    public void pausePlayback() {
        if(isPlaying()) {
            isPlayingProperty.set(false);
            if (timeline != null) {
                timeline.stop();
                MidiManager.getInstance().allNotesOff();
                PianoGrid.getPlayhead().setVisible(false);
            }
        }
    }

    public void stopPlayback() {
        pausePlayback();
        pausedBeats = 0;
    }

    public void buildTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        record MidiEvent(double time, Note note, boolean isOn) {}
        ArrayList<MidiEvent> midiEvents = new ArrayList<>();

        ArrayList<Note> notes = new ArrayList<>(NoteManager.getInstance().getNotesList());
        int trackLength = GridRenderer.getInstance().getSignatureProperty().get();

        if(!notes.isEmpty()) {
            double lastNoteEnd = notes.getLast().getColumn() + notes.getLast().getLength();
            if(lastNoteEnd % GridRenderer.getInstance().getSignatureProperty().get() == 0) trackLength = (int) lastNoteEnd;
            else trackLength =
                    (int) ((lastNoteEnd+GridRenderer.getInstance().getSignatureProperty().get()) -
                            (lastNoteEnd % GridRenderer.getInstance().getSignatureProperty().get()));
        }

        for(Note note : notes) {
            double startTime = note.getColumn() * getMsPerBeat();
            double endTime = startTime + (note.getLength()*getMsPerBeat());
            midiEvents.add(new MidiEvent(startTime, note, true));
            midiEvents.add(new MidiEvent(endTime, note, false));
        }

        for(MidiEvent event : midiEvents) {
            KeyFrame frame = new KeyFrame(Duration.millis(event.time), _ -> {
                if(event.isOn) MidiManager.getInstance().playNote(event.note);
                else MidiManager.getInstance().stopNote(event.note);
            });
            timeline.getKeyFrames().add(frame);
        }

        double updateInterval = 10;
        double totalTime = trackLength * getMsPerBeat();

        for(double t = 0; t <= totalTime; t += updateInterval) {
            double progress = t/totalTime;
            double x = (trackLength*CELL_WIDTH*progress);

            KeyFrame frame = new KeyFrame(Duration.millis(t), _ -> {
                PianoGrid.getPlayhead().setStartX(x * PianoGridPane.zoomX.get());
                pausedBeats = timeline.getCurrentTime().toMillis() / getMsPerBeat();
            });
            timeline.getKeyFrames().add(frame);
        }
    }

    private double getMsPerBeat() {
        return 60000.0 / bpmProperty.get();
    }

    public void updateNotes() {
        if(isPlaying()) {
            if(timeline != null) timeline.stop();
            buildTimeline();
            timeline.play();
            timeline.jumpTo(Duration.millis(pausedBeats * getMsPerBeat()));
        }
    }
}
