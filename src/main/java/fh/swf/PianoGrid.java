package fh.swf;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static fh.swf.Main.mainPane;

public class PianoGrid extends Pane {
    private double zoom = 1.0;

    private boolean isDragging = false;
    private Timeline timeline;
    @Getter private final SimpleBooleanProperty isPlaying = new SimpleBooleanProperty(false);
    private final Line playhead;
    private int currentBeat = 0;
    @Getter private final List<NoteEvent> noteEvents = new ArrayList<>();

    public static final String[] TONES = {"B", "A#", "A", "G#", "G", "F#", "F", "E", "D#", "D", "C#", "C"};
    public static final int OCTAVES = 7;
    private final SimpleIntegerProperty signatureProperty = new SimpleIntegerProperty(4);
    private final SimpleIntegerProperty strokeAmountProperty = new SimpleIntegerProperty(18);

    public static final int CELL_WIDTH = 50;
    public static final int CELL_HEIGHT = 25;

    private final ArrayList<Line> horizontalLines = new ArrayList<>();
    private final ArrayList<Line> verticalLines = new ArrayList<>();

    private final Pane grid;

    public PianoGrid() {
        setFocusTraversable(false);
        setPrefWidth(signatureProperty.get() * strokeAmountProperty.get() * CELL_WIDTH + 1);
        setPrefHeight(TONES.length * (OCTAVES-1) * CELL_HEIGHT);
        grid = new Pane();
        grid.setPickOnBounds(false);
        getChildren().add(grid);
        playhead = new Line();
        playhead.setStroke(Color.RED);
        playhead.setStrokeWidth(2);
        playhead.setStartY(0);
        playhead.endYProperty().bind(heightProperty());
        playhead.endXProperty().bind(playhead.startXProperty());
        playhead.setVisible(false);
        getChildren().add(playhead);

        isPlaying.addListener((_,_,_) -> mainPane.getMenuBar().getPlayButton().changeGraphic());

        setOnMouseClicked(this::onMouseClickedEvent);
        setOnMouseDragged(this::onMouseDraggedEvent);
        setOnMouseReleased(this::onMouseReleasedEvent);
    }
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        drawHorizontalLines();
        drawVerticalLines();
    }

    private void drawHorizontalLines() {
        grid.getChildren().removeAll(horizontalLines);
        horizontalLines.clear();

        double width = getWidth();
        if (width <= 0) return;

        for (int row = 0; row < TONES.length * (OCTAVES-1); row++) {
            double y = (row + 1) * CELL_HEIGHT * zoom;
            Line hLine = new Line(0, y, width, y);
            hLine.setStroke(Color.GRAY);
            hLine.setStrokeWidth((row + 1) % 12 == 0 ? 1 : 0.5);
            horizontalLines.add(hLine);
        }

        grid.getChildren().addAll(horizontalLines);
    }

    private void drawVerticalLines() {
        grid.getChildren().removeAll(verticalLines);
        verticalLines.clear();

        double height = getHeight();
        if (height <= 0) return;

        for (int col = 0; col < signatureProperty.get() * strokeAmountProperty.get(); col++) {
            double x = (col + 1) * CELL_WIDTH * zoom;
            Line vLine = new Line(x, 0, x, height);
            vLine.setStroke(Color.BLACK);
            vLine.setStrokeWidth((col + 1) % signatureProperty.get() == 0 ? 1 : 0.5);
            verticalLines.add(vLine);
        }

        grid.getChildren().addAll(verticalLines);
    }

    private void onMouseDraggedEvent(MouseEvent event) {
        isDragging = true;
    }
    private void onMouseReleasedEvent(MouseEvent event) {
        isDragging = false;
    }

    private void onMouseClickedEvent(MouseEvent event) {
        if (event.getTarget() == this && !isDragging) {
            int col = (int) (event.getX() / (CELL_WIDTH * zoom));
            int row = (int) (event.getY() / (CELL_HEIGHT * zoom));

            NoteEvent noteEvent = new NoteEvent(col, row, 1);
            noteEvents.add(noteEvent);

            double snappedX = col * CELL_WIDTH * zoom;
            double snappedY = row * CELL_HEIGHT * zoom;

            NoteView note = new NoteView(noteEvent);
            note.setLayoutX(snappedX);
            note.setLayoutY(snappedY);
            note.setPrefWidth(CELL_WIDTH * zoom);
            note.setPrefHeight(CELL_HEIGHT * zoom);
            getChildren().add(note);

            if(!isPlaying.get()) {
                MidiManager.getInstance().noteOn(noteEvent.midiNote, 100);

                PauseTransition pause = new PauseTransition(Duration.millis(200)); // Länge des "dum"
                pause.setOnFinished(_ -> MidiManager.getInstance().noteOff(noteEvent.midiNote));
                pause.play();
            }

            updateNotes();
        }
    }


    public void startPlayback() {
        isPlaying.set(true);
        System.err.println("Hallo");
        playhead.setVisible(true);
        buildTimeline();
        playhead.setStartX(currentBeat * CELL_WIDTH * zoom);
        timeline.play();
        timeline.jumpTo(Duration.millis(currentBeat * getMsPerBeat()));
    }

    public void updateNotes() {
        if(isPlaying.get()) {
            if(timeline != null) timeline.stop();

            buildTimeline(0);

            playhead.setStartX(currentBeat * CELL_WIDTH* zoom);

            timeline.play();
            timeline.jumpTo(Duration.millis(currentBeat * getMsPerBeat()));
        }
    }

    public void changeBpm() {
        if(isPlaying.get()) {
            if (timeline != null) {
                currentBeat = getCurrentBeatFromPlayhead();
                timeline.stop();
            }
            buildTimeline();
            playhead.setStartX(currentBeat * CELL_WIDTH * zoom);
            timeline.play();

            timeline.jumpTo(Duration.millis(currentBeat * getMsPerBeat()));
        }
    }
    private void buildTimeline() {
        int startBeat = 0;
        buildTimeline(startBeat);
    }


    private void buildTimeline(int startBeat) {
        double msPerBeat = getMsPerBeat();
        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        record MidiEvent(double time, int midiNote, boolean isOn) {}

        List<MidiEvent> midiEvents = new ArrayList<>();

        noteEvents.sort(Comparator
                .comparingInt(note -> note.column));

        int endBeat = signatureProperty.get();
        if(!noteEvents.isEmpty()) {
            double lastNoteEnd = noteEvents.getLast().column + noteEvents.getLast().length;
            if (lastNoteEnd % signatureProperty.get() == 0) endBeat = (int) lastNoteEnd;
            else endBeat = (int) (lastNoteEnd + signatureProperty.get() - lastNoteEnd % signatureProperty.get());
        }
        for (NoteEvent note : noteEvents) {
            if (note.column < startBeat || note.column >= endBeat) continue;
            double startTime = (note.column - startBeat) * msPerBeat;
            double endTime = startTime + note.length * msPerBeat;

            midiEvents.add(new MidiEvent(startTime, note.midiNote, true));
            midiEvents.add(new MidiEvent(endTime, note.midiNote, false));
        }

        for (MidiEvent event : midiEvents) {
            KeyFrame frame = new KeyFrame(Duration.millis(event.time), _ -> {
                if (event.isOn)
                    MidiManager.getInstance().noteOn(event.midiNote, 100);
                else
                    MidiManager.getInstance().noteOff(event.midiNote);
            });
            timeline.getKeyFrames().add(frame);
        }


        int totalBeats = endBeat - startBeat;
        for (int i = 0; i <= totalBeats; i++) {
            double time = i * msPerBeat;
            double x = (startBeat + i) * CELL_WIDTH;
            int finalI = i;
            KeyFrame playheadFrame = new KeyFrame(Duration.millis(time), _ -> {
                playhead.setStartX(x * zoom);
                currentBeat = startBeat + finalI;
            });
            timeline.getKeyFrames().add(playheadFrame);
        }

    }




    private int getCurrentBeatFromPlayhead() {
        double x = playhead.getStartX();
        return (int) (x / (CELL_WIDTH * zoom));
    }

    private int getCurrentBpm() {
        try {
            return Math.max(30, Integer.parseInt(mainPane.getMenuBar().getBpmField().getText()));
        } catch (NumberFormatException e) {
            return 120;
        }
    }

    public void pausePlayback() {
        if (timeline != null) {
            isPlaying.set(false);
            timeline.stop();
            currentBeat = getCurrentBeatFromPlayhead();
            playhead.setVisible(false);
            for(NoteEvent note : noteEvents) {
                MidiManager.getInstance().stopNote(note.midiNote);
            }
        }
    }

    public void stopPlayback() {
        if(timeline != null) {
            isPlaying.set(false);
            timeline.stop();
            currentBeat = 0;
            playhead.setVisible(false);
            for(NoteEvent note : noteEvents) {
                MidiManager.getInstance().stopNote(note.midiNote);
            }
        }
    }

    private double getMsPerBeat() { return 60000.0 / getCurrentBpm(); }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        updateGridSize();
    }

    private void updateGridSize() {
        for(Node n : getChildren()) {
            if(n instanceof  NoteView note) {
                note.setZoom(zoom);
            } else if (n == playhead) {
                playhead.setStartX(currentBeat * CELL_WIDTH * zoom);
            }
        }

        drawVerticalLines();
        drawHorizontalLines();

        setPrefWidth(signatureProperty.get() * strokeAmountProperty.get() * CELL_WIDTH * zoom);
        setPrefHeight(TONES.length * (OCTAVES-1) * CELL_HEIGHT * zoom);
    }
}
