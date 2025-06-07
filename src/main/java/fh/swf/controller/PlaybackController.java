package fh.swf.controller;

import fh.swf.Note;
import fh.swf.PianoGrid;
import fh.swf.model.manager.MidiManager;
import fh.swf.model.manager.NoteManager;
import fh.swf.render.GridRenderer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import lombok.Getter;

import java.util.ArrayList;

import static fh.swf.render.GridRenderer.CELL_WIDTH;

public class PlaybackController {
    private static PlaybackController instance;


    private Timeline timeline;
    private Duration pauseTime;
    @Getter private final SimpleBooleanProperty isPlayingProperty;
    @Getter private final SimpleIntegerProperty bpmProperty;

    public boolean isPlaying() { return isPlayingProperty.get(); }

    public static PlaybackController getInstance() {
        if(instance == null)
            instance = new PlaybackController();
        return instance;
    }

    private PlaybackController() {
        isPlayingProperty = new SimpleBooleanProperty(false);
        bpmProperty = new SimpleIntegerProperty(120);
        pauseTime = new Duration(0);
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
                for (Note note : NoteManager.getInstance().getNotes())
                    MidiManager.getInstance().stopNote(note);

                PianoGrid.getPlayhead().setVisible(false);
            }
        }
    }

    public void stopPlayback() {
        pausePlayback();
        pauseTime = Duration.millis(0);
    }

    public void buildTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        record MidiEvent(double time, Note note, boolean isOn) {}
        ArrayList<MidiEvent> midiEvents = new ArrayList<>();

        ArrayList<Note> notes = new ArrayList<>(NoteManager.getInstance().getNotes());
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
                PianoGrid.getPlayhead().setStartX(x * GridRenderer.zoom);
                pauseTime = timeline.getCurrentTime();
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
            timeline.jumpTo(pauseTime);
        }
    }
}
