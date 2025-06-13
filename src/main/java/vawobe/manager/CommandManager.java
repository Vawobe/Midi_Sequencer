package vawobe.manager;

import vawobe.commands.SequencerCommand;

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
        boolean isPlaying = PlaybackManager.getInstance().isPlaying();
        if(isPlaying) PlaybackManager.getInstance().pausePlayback();
        command.execute();
        undoStack.push(command);
        redoStack.clear();
        if(isPlaying) PlaybackManager.getInstance().startPlayback();
    }

    public void undo() {
        if(!undoStack.isEmpty()) {
            boolean isPlaying = PlaybackManager.getInstance().isPlaying();
            if(isPlaying) PlaybackManager.getInstance().pausePlayback();
            SequencerCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
            if(isPlaying) PlaybackManager.getInstance().startPlayback();
        }
    }

    public void redo() {
        if(!redoStack.isEmpty()) {
            boolean isPlaying = PlaybackManager.getInstance().isPlaying();
            if(isPlaying) PlaybackManager.getInstance().pausePlayback();
            SequencerCommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
            if(isPlaying) PlaybackManager.getInstance().startPlayback();
        }
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
