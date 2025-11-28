package minesweeper.command;

import minesweeper.model.Board;
import minesweeper.model.Cell;
import minesweeper.model.GameModel;
import minesweeper.model.GameState;

import java.util.ArrayList;
import java.util.List;

public class RevealCommand implements GameCommand {
    private final GameModel gameModel;
    private final int row;
    private final int col;
    private final List<Cell> revealedCells;
    private boolean causedLoss;

    public RevealCommand(GameModel gameModel, int row, int col) {
        this.gameModel = gameModel;
        this.row = row;
        this.col = col;
        this.revealedCells = new ArrayList<>();
        this.causedLoss = false;
    }

    @Override
    public void execute() {
        revealedCells.clear();
        Board board = gameModel.getBoard();
        board.ensureMinesInitialized(row, col);
        
        Cell cell = board.getCell(row, col);
        if (cell.isRevealed() || cell.isFlagged()) {
            return;
        }

        if (cell.isMine()) {
            cell.setRevealed(true);
            revealedCells.add(cell);
            gameModel.setState(GameState.LOST);
            causedLoss = true;
        } else {
            floodFill(board, row, col);
            gameModel.checkWinCondition();
        }
        gameModel.notifyObservers();
    }

    private void floodFill(Board board, int r, int c) {
        if (!board.isValid(r, c)) return;
        Cell cell = board.getCell(r, c);
        if (cell.isRevealed() || cell.isFlagged()) return;

        cell.setRevealed(true);
        revealedCells.add(cell);

        if (cell.getNeighborMineCount() == 0) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    if (dr == 0 && dc == 0) continue;
                    floodFill(board, r + dr, c + dc);
                }
            }
        }
    }

    @Override
    public void undo() {
        for (Cell cell : revealedCells) {
            cell.setRevealed(false);
        }
        if (causedLoss) {
            gameModel.setState(GameState.PLAYING);
        }
        gameModel.notifyObservers();
    }
}
