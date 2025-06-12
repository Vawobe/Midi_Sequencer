package vawobe.model.manager;

import vawobe.commands.SequencerCommand;
import vawobe.controller.PlaybackController;

import java.util.Stack;

public class CommandManager {
    private static CommandManager instance;

    private final Stack<SequencerCommand> undoStack;
    private final Stack<SequencerCommand> redoStack;

    public static CommandManager getInstance() {
        if(instance == null)
            instance = new CommandManager();
        return instance;
    }

    private CommandManager() {
        undoStack = new Stack<>();
        redoStack = new Stack<>();
    }

    public void executeCommand(SequencerCommand command) {
        boolean isPlaying = PlaybackController.getInstance().isPlaying();
        if(isPlaying) PlaybackController.getInstance().pausePlayback();
        command.execute();
        undoStack.push(command);
        redoStack.clear();
        if(isPlaying) PlaybackController.getInstance().startPlayback();
    }

    public void undo() {
        if(!undoStack.isEmpty()) {
            boolean isPlaying = PlaybackController.getInstance().isPlaying();
            if(isPlaying) PlaybackController.getInstance().pausePlayback();
            SequencerCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            if(isPlaying) PlaybackController.getInstance().startPlayback();
        }
    }

    public void redo() {
        if(!redoStack.isEmpty()) {
            boolean isPlaying = PlaybackController.getInstance().isPlaying();
            if(isPlaying) PlaybackController.getInstance().pausePlayback();
            SequencerCommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            if(isPlaying) PlaybackController.getInstance().startPlayback();
        }
    }
}
