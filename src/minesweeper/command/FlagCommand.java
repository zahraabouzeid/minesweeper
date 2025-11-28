package minesweeper.command;

import minesweeper.model.Cell;
import minesweeper.model.GameModel;

public class FlagCommand implements GameCommand {
    private final GameModel gameModel;
    private final int row;
    private final int col;
    private boolean wasFlagged;

    public FlagCommand(GameModel gameModel, int row, int col) {
        this.gameModel = gameModel;
        this.row = row;
        this.col = col;
    }

    @Override
    public void execute() {
        Cell cell = gameModel.getBoard().getCell(row, col);
        if (cell.isRevealed()) return;

        wasFlagged = cell.isFlagged();
        cell.setFlagged(!wasFlagged);
        
        if (cell.isFlagged()) {
            gameModel.setFlagsPlaced(gameModel.getFlagsPlaced() + 1);
        } else {
            gameModel.setFlagsPlaced(gameModel.getFlagsPlaced() - 1);
        }
    }

    @Override
    public void undo() {
        Cell cell = gameModel.getBoard().getCell(row, col);
        cell.setFlagged(wasFlagged);
        
        if (wasFlagged) {
             gameModel.setFlagsPlaced(gameModel.getFlagsPlaced() + 1);
        } else {
             gameModel.setFlagsPlaced(gameModel.getFlagsPlaced() - 1);
        }
    }
}
