package fh.swf.controller;

import fh.swf.Note;
import fh.swf.PianoGrid;
import fh.swf.Playhead;
import fh.swf.model.manager.MidiManager;
import fh.swf.model.manager.NoteManager;
import fh.swf.render.GridRenderer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

import static fh.swf.render.GridRenderer.CELL_WIDTH;

public class PlaybackController {
    private static PlaybackController instance;

    private Timeline timeline;
    @Getter private final SimpleBooleanProperty isPlayingProperty;
    @Getter @Setter private int currentBeat = 0;
    private final SimpleIntegerProperty bpmProperty;

    public boolean isPlaying() { return isPlayingProperty.get(); }

    public static PlaybackController getInstance() {
        if(instance == null)
            instance = new PlaybackController();
        return instance;
    }

    private PlaybackController() {
        isPlayingProperty = new SimpleBooleanProperty(false);
        bpmProperty = new SimpleIntegerProperty(120);
    }

    public void startPlayback() {
        if(!isPlaying()) {
            Playhead playhead = PianoGrid.getPlayhead();
            isPlayingProperty.set(true);
            playhead.setVisible(true);
            buildTimeline();
            playhead.setStartX(currentBeat * CELL_WIDTH * GridRenderer.zoom);
            timeline.play();
            timeline.jumpTo(Duration.millis(currentBeat * getMsPerBeat()));
        }
    }

    public void pausePlayback() {
        if(isPlaying()) {
            if (timeline != null) {
                Playhead playhead = PianoGrid.getPlayhead();
                isPlayingProperty.set(false);
                timeline.stop();
                currentBeat = playhead.getCurrentBeat();
                playhead.setVisible(false);
                for (Note note : NoteManager.getInstance().getNotes())
                    MidiManager.getInstance().stopNote(note);
            }
        }
    }

    public void stopPlayback() {
        if(isPlaying()) {
            if (timeline != null) {
                Playhead playhead = PianoGrid.getPlayhead();
                isPlayingProperty.set(false);
                timeline.stop();
                currentBeat = 0;
                playhead.setVisible(false);
                for (Note note : NoteManager.getInstance().getNotes())
                    MidiManager.getInstance().stopNote(note);
            }
        }
    }

    public void changeBpm() {
        if(isPlaying()) {
            Playhead playhead = PianoGrid.getPlayhead();
            if(timeline != null) {
                currentBeat = playhead.getCurrentBeat();
                timeline.stop();
            }

            buildTimeline();
            playhead.setStartX(currentBeat * CELL_WIDTH * GridRenderer.zoom);
            timeline.play();
            timeline.jumpTo(Duration.millis(currentBeat * getMsPerBeat()));
        }
    }

    public void buildTimeline() {
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        record MidiEvent(double time, Note note, boolean isOn) {}
        ArrayList<MidiEvent> midiEvents = new ArrayList<>();

        int endBeat = GridRenderer.getInstance().getSignatureProperty().get();

        ArrayList<Note> notes = new ArrayList<>(NoteManager.getInstance().getNotes());
        if(!notes.isEmpty()) {
            double lastNoteEnd = notes.getLast().getColumn() + notes.getLast().getLength();
            if(lastNoteEnd % GridRenderer.getInstance().getSignatureProperty().get() == 0) endBeat = (int) lastNoteEnd;
            else endBeat = (int) (lastNoteEnd+GridRenderer.getInstance().getSignatureProperty().get()-lastNoteEnd
                    % GridRenderer.getInstance().getSignatureProperty().get());
        }
        for(Note note : notes) {
            if(note.getColumn() < 0 || note.getColumn() >= endBeat) continue;
            double startTime = (note.getColumn() - 0) * getMsPerBeat();
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
        double totalTime = endBeat * getMsPerBeat();

        for(double t = 0; t <= totalTime; t += updateInterval) {
            double progress = t/totalTime;
            double x = (endBeat*CELL_WIDTH*progress);
            KeyFrame frame = new KeyFrame(Duration.millis(t), _ -> {
                PianoGrid.getPlayhead().setStartX(x * GridRenderer.zoom);
                currentBeat = (int) Math.round(progress);
            });
            timeline.getKeyFrames().add(frame);
        }
    }

    private double getMsPerBeat() {
        return 60000.0 / bpmProperty.get();
    }

    public void updateNotes() {
        currentBeat = PianoGrid.getPlayhead().getCurrentBeat();
        if(isPlaying()) {
            if(timeline != null) timeline.stop();
            buildTimeline();
            PianoGrid.getPlayhead().setStartX(currentBeat * CELL_WIDTH * GridRenderer.zoom);
            timeline.play();
            timeline.jumpTo(Duration.millis(currentBeat * getMsPerBeat()));
        }
    }
}
