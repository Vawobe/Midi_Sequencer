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
        System.out.println(command);
        command.execute();
        undoStack.push(command);
        redoStack.clear();
    }

    public void undo() {
        if(!undoStack.isEmpty()) {
            SequencerCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if(!redoStack.isEmpty()) {
            SequencerCommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }
}
