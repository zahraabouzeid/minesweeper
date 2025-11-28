package minesweeper.controller;

import minesweeper.command.CommandInvoker;
import minesweeper.command.FlagCommand;
import minesweeper.command.GameCommand;
import minesweeper.command.RevealCommand;
import minesweeper.model.GameModel;
import minesweeper.model.GameState;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GameController {
    private final GameModel gameModel;
    private final CommandInvoker commandInvoker;
    private Timer replayTimer;
    private boolean isReplaying;

    public GameController(GameModel gameModel) {
        this.gameModel = gameModel;
        this.commandInvoker = new CommandInvoker();
        this.isReplaying = false;
    }

    public boolean isReplaying() {
        return isReplaying;
    }

    public void leftClick(int row, int col) {
        if (gameModel.getState() != GameState.PLAYING) return;
        commandInvoker.executeCommand(new RevealCommand(gameModel, row, col));
    }

    public void rightClick(int row, int col) {
        if (gameModel.getState() != GameState.PLAYING) return;
        commandInvoker.executeCommand(new FlagCommand(gameModel, row, col));
    }

    public void undo() {
        if (gameModel.getState() != GameState.PLAYING) return;
        commandInvoker.undo();
    }

    public void redo() {
        if (gameModel.getState() != GameState.PLAYING) return;
        commandInvoker.redo();
    }

    public void startReplay() {
        if (replayTimer != null && replayTimer.isRunning()) return;

        List<GameCommand> history = commandInvoker.getCommandHistory();
        if (history.isEmpty()) {
            return;
        }
        

        resetModelForReplay();
        isReplaying = true;
        gameModel.setReplaying(true);
        
        final int[] commandIndex = {0};
        replayTimer = new Timer(200, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (commandIndex[0] < history.size()) {
                    GameCommand cmd = history.get(commandIndex[0]);
                    cmd.execute();
                    commandIndex[0]++;
                } else {
                    replayTimer.stop();
                    isReplaying = false;
                    gameModel.setReplaying(false);
                    // JOptionPane.showMessageDialog(null, "Replay Finished!");
                }
            }
        });
        replayTimer.start();
    }

    private void resetModelForReplay() {
        int rows = gameModel.getBoard().getRows();
        int cols = gameModel.getBoard().getCols();
        for(int r=0; r<rows; r++) {
            for(int c=0; c<cols; c++) {
                gameModel.getBoard().getCell(r, c).setRevealed(false);
                gameModel.getBoard().getCell(r, c).setFlagged(false);
            }
        }
        gameModel.resetTimer();
        gameModel.setState(GameState.PLAYING);
        gameModel.setFlagsPlaced(0);
        gameModel.notifyObservers();
    }
}
