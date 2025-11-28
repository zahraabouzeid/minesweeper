package minesweeper.model;

public class Cell {
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int neighborMineCount;
    private final int row;
    private final int col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.neighborMineCount = 0;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }

    public int getNeighborMineCount() {
        return neighborMineCount;
    }

    public void setNeighborMineCount(int neighborMineCount) {
        this.neighborMineCount = neighborMineCount;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
