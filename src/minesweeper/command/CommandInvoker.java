package minesweeper.command;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CommandInvoker {
    private final Stack<GameCommand> undoStack = new Stack<>();
    private final Stack<GameCommand> redoStack = new Stack<>();
    private final List<GameCommand> commandHistory = new ArrayList<>();

    public void executeCommand(GameCommand command) {
        command.execute();
        undoStack.push(command);
        commandHistory.add(command);
        redoStack.clear();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            GameCommand command = undoStack.pop();
            command.undo();
            redoStack.push(command);
        }
    }

    public void redo() {
        if (!redoStack.isEmpty()) {
            GameCommand command = redoStack.pop();
            command.execute();
            undoStack.push(command);
        }
    }

    public List<GameCommand> getCommandHistory() {
        return new ArrayList<>(commandHistory);
    }
    
    public void reset() {
        undoStack.clear();
        redoStack.clear();
        commandHistory.clear();
    }
}
