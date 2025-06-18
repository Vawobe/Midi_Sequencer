package vawobe;

import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import vawobe.manager.PlaybackManager;
import vawobe.render.GridRenderer;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static vawobe.Main.gridLineColor;
import static vawobe.Main.mainColor;
import static vawobe.render.GridRenderer.CELL_WIDTH;

public class SignatureLine extends BasicScrollPane {
    private final Pane content;
    private final ArrayList<Line> tactLines;
    private final ArrayList<Text> tactNums;

    private final int HEIGHT = 20;

    public SignatureLine(PianoGridPane pianoGridPane) {
        super();

        tactLines = new ArrayList<>();
        tactNums = new ArrayList<>();
        prefWidthProperty().bind(pianoGridPane.getPianoGridScrollPane().prefWidthProperty());
        widthProperty().addListener(e -> drawLines());

        content = new Pane();
        content.prefWidthProperty().bind(pianoGridPane.getPianoGrid().prefWidthProperty());
        setMinHeight(HEIGHT);
        setMaxHeight(HEIGHT);
        content.setPrefHeight(HEIGHT);
        content.setBackground(new Background(new BackgroundFill(mainColor, null, null)));

        setContent(content);
        hvalueProperty().bindBidirectional(pianoGridPane.getPianoGridScrollPane().hvalueProperty());

        drawLines();

        GridRenderer.getInstance().getSignatureProperty().addListener((obs,oldV,e) -> drawLines());

        content.getChildren().add(PianoGrid.getPlayhead().getHead());

        content.setOnMouseMoved(event -> {
            double x = event.getX();
            double timeInSeconds = calculateTimeAtX(x);
            if(getTooltip() == null) setTooltip(new Tooltip());
            getTooltip().setText(formatTime(timeInSeconds));
            getTooltip().show(content, event.getScreenX() + 10, event.getScreenY() + 10);
        });

        content.setOnMouseExited(e -> getTooltip().hide());

        content.setOnMousePressed(this::onMousePressedEvent);
    }

    public void drawLines() {
        content.getChildren().removeAll(tactLines);
        content.getChildren().removeAll(tactNums);
        tactLines.clear();
        tactNums.clear();

        for(int signature = 0; signature <= GridRenderer.getInstance().getStrokeAmountProperty().get(); signature++) {
            double x = signature * CELL_WIDTH * PianoGridPane.zoomX.get() * (GridRenderer.getInstance().getSignatureProperty().get());
            Line line = new Line(x,0,x,HEIGHT);
            line.setStrokeWidth(2);
            line.setStroke(gridLineColor);
            tactLines.add(line);
            Text tact = new Text(Integer.toString(signature+1));
            tact.setFill(Color.WHITE);
            tact.setY(15);
            tact.setX(x+5);
            tactNums.add(tact);
        }

        content.getChildren().addAll(tactLines);
        content.getChildren().addAll(tactNums);
        PianoGrid.getPlayhead().getHead().toFront();
    }

    private double calculateTimeAtX(double x) {
        double zoom = PianoGridPane.zoomX.get();
        int bpm = PlaybackManager.getInstance().getBpmProperty().get();

        double beatsPerSecond = bpm / 60.0;
        double pixelsPerBeat = (double) CELL_WIDTH * zoom;

        double beatPosition = x / pixelsPerBeat;
        return beatPosition / beatsPerSecond;
    }

    private String formatTime(double seconds) {
        int totalSeconds = (int) seconds;
        int minutes = totalSeconds / 60;
        int secs = totalSeconds % 60;
        int millis = (int) ((seconds - totalSeconds) * 1000);
        return String.format("%02d:%02d.%03d", minutes, secs, millis);
    }

    private void onMousePressedEvent(MouseEvent event) {
        double x = event.getX();
        double timeInSeconds = calculateTimeAtX(x);

        PlaybackManager playback = PlaybackManager.getInstance();
        playback.startPlaybackAtSeconds(timeInSeconds);
    }
}
