package vawobe;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import vawobe.icons.*;

import static vawobe.Main.mainColor;
import static vawobe.Main.mainPane;

public class HelpPane extends BorderPane {
    private final GridPane content;
    private int rows = 0;

    public HelpPane() {
        Button closeButton = new Button("✖");
        closeButton.setTextFill(Color.LIGHTGRAY);
        closeButton.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, null, null)));
        closeButton.hoverProperty().addListener((obs,oldV,newValue) ->
                closeButton.setTextFill(newValue ? Color.WHITE : Color.LIGHTGRAY));
        closeButton.setOnAction(e -> mainPane.setRight(null));

        HBox topBar = new HBox();
        topBar.getChildren().add(closeButton);
        topBar.setAlignment(Pos.TOP_RIGHT);
        setTop(topBar);


        content = new GridPane();
        content.setHgap(5);
        content.setPadding(new Insets(5));
        BasicScrollPane scrollPane = new BasicScrollPane();
        content.setBackground(new Background(new BackgroundFill(mainColor, null, null)));
        scrollPane.setContent(content);
        setCenter(scrollPane);

        addTitle("Basics");
        addPoint("Play Button", new PlayIcon(0.7), "Start Playback.");
        addPoint("Pause Button", new PauseIcon(0.7), "Pause Playback.");
        addPoint("Stop Button", new StopIcon(0.7), "Stops Playback.");
        addPoint("BPM Field", null, "Change the BPM to a number between 10 and 999.");
        addPoint("Title Field", null, "Change the title of your song.");
        addPoint("Draw Button", new DrawIcon(0.8), "Change into Draw Mode.");
        addPoint("Select Button", new SelectIcon(0.8), "Change into Selection Mode.");
        addPoint("Erase Button", new EraseIcon(0.8), "Change into Erase Mode.");
        addPoint("Drag-To-Scroll Button", new DragToScrollIcon(0.8), "Change into Drag-To-Scroll Mode.");
        addPoint("Instrument Dropdown", null, "Change the Instrument you are currently drawing with.");
        addPoint("Cut Button", new CutIcon(0.7), "Cut your currently selected Notes.");
        addPoint("Copy Button", new CopyIcon(0.7), "Copy your currently selected Notes.");
        addPoint("Paste Button", new PasteIcon(0.7), "Paste your copied notes.");
        addPoint("Select-All Button", new SelectAllIcon(0.7), "Select all drawn notes.");
        addPoint("Zoom-Out Button", new ZoomOutIcon(0.5), "Zoom out.");
        addPoint("Zoom-In Button", new ZoomInIcon(0.5), "Zoom in.");
        addPoint("Save Button", new SaveIcon(0.7), "Save your creation as a .midfx. You can also export your file as MIDI, WAV or MP3 by clicking the arrow and selecting your wanted option.");
        addPoint("Load Button", new LoadIcon(0.7), "Load a .midfx or import a MIDI.");
        addPoint("Volume Slider", null, "Change the volume of the whole song.");
        addPoint("Measure Bar", null, "The measure bar shows you, where the beats begin. Hovering over a position on the measure bar shows you the time of your hover position. Clicking anywhere on the measure bar will place the position of the playhead at the clicked position and start the playback.");
        addPoint("Grid Menu", null, "Change the Grid. This also allows you to draw notes in the length of the selected grid width.");
        addPoint("Time Signature Menu", null, "Change the signature of your song.");
        addPoint("Mouse Scroll", null, "Use the mouse scroll to scroll through the grid.");
        addPoint("Pinch gesture", null, "Use the pinch gesture to zoom in and out.");
        addPoint("Drag note", null, "Move selected notes on the grid.");
        addPoint("RMB on note", null, "Delete the clicked note.");
        addPoint("Drag right edge of a note", null, "Change the length of the note.");
        addSpacer(20);

        addTitle("Modes");
        addSubTitle("Draw Mode");
        addPoint("LMB on free space", null, "Place a note in the clicked cell.");
        addSpacer(10);

        addSubTitle("Select Mode");
        addPoint("Drag over free space", null, "Span a selection rectangle. Releasing the mouse button will select every note touched by the selection rectangle.");
        addSpacer(10);

        addSubTitle("Erase Mode");
        addPoint("LMB or RMB on note", null, "Delete the clicked note.");
        addSpacer(10);

        addSubTitle("Drag-To-Scroll Mode");
        addPoint("Drag over free space", null, "Scroll through the grid.");
        addSpacer(20);

        addTitle("Shortcuts");
        addPoint("Ctrl + A", null, "Select all drawn notes.");
        addPoint("Ctrl + C", null, "Copy selected notes.");
        addPoint("Ctrl + X", null, "Cut selected notes.");
        addPoint("Ctrl + V", null, "Paste your copied notes.");
        addPoint("Ctrl + Alt + V", null, "Paste your copied notes at your cursors x location.");
        addPoint("Ctrl + Z", null, "Undo your previous action.");
        addPoint("Ctrl + Y", null, "Redo your previous undone action.");
        addPoint("Delete or Backspace", null, "Delete your selected notes.");
        addPoint("Space or Enter", null, "Play or pause the playback.");
        addPoint("Escape", null, "Stop the playback.");
        addPoint("Arrow keys", null, "Scroll through the grid.");
        addPoint("Tab", null, "Change the current mode.");
        addPoint("Ctrl + Click on note", null, "Select the instrument of the clicked note.");
        addPoint("Ctrl + Alt + Click on note", null, "Select all notes using the instrument of the clicked note.");
        addPoint("Alt + Scroll or Alt + +/-", null, "Change the volume of the selected notes.");
        addPoint("Ctrl + Arrow keys", null, "Move all selected notes on the grid.");

    }

    private Text boldText(String string) {
        Text text = normalText(string);
        text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, 20));
        return text;
    }

    private Text normalText(String string) {
        Text text = new Text(string);
        text.setFill(Color.WHITE);
        return text;
    }

    private void addTitle(String name) {
        Text text = boldText(name);
        GridPane.setHalignment(text, HPos.CENTER);
        content.add(text,0,rows);
        GridPane.setColumnSpan(text, 3);
        rows++;
    }

    private void addSubTitle(String name) {
        Text text = normalText(name);
        text.setFont(Font.font(text.getFont().getFamily(), FontWeight.BOLD, 15));
        content.add(text,0,rows);
        GridPane.setColumnSpan(text, 3);
        rows++;
    }

    private void addPoint(String name, Node icon, String text) {
        Text nameText = normalText(name);
        GridPane.setValignment(nameText, VPos.TOP);
        content.add(nameText,0,rows);
        if(icon != null) {
            GridPane.setValignment(icon, VPos.TOP);
            content.add(icon, 1, rows);
        }
        Text stringText = normalText(text);
        stringText.setWrappingWidth(200);
        content.add(stringText, 2,rows);
        rows++;
    }

    private void addSpacer(int height) {
        Rectangle rectangle = new Rectangle(1, height);
        rectangle.setFill(Color.TRANSPARENT);
        content.add(rectangle, 0, rows);
        rows++;
    }
}
