package minesweeper.model;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final int rows;
    private final int cols;
    private final int mineCount;
    private final Cell[][] cells;
    private final BoardGenerator boardGenerator;
    private boolean minesInitialized;

    public Board(int rows, int cols, int mineCount, BoardGenerator boardGenerator) {
        this.rows = rows;
        this.cols = cols;
        this.mineCount = mineCount;
        this.boardGenerator = boardGenerator;
        this.cells = new Cell[rows][cols];
        this.minesInitialized = false;
        initializeCells();
    }

    private void initializeCells() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                cells[r][c] = new Cell(r, c);
            }
        }
    }

    public void ensureMinesInitialized(int firstClickRow, int firstClickCol) {
        if (!minesInitialized) {
            boardGenerator.generateMines(this, mineCount, firstClickRow, firstClickCol);
            calculateNeighborMineCounts();
            minesInitialized = true;
        }
    }

    private void calculateNeighborMineCounts() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!cells[r][c].isMine()) {
                    int count = countMinesAround(r, c);
                    cells[r][c].setNeighborMineCount(count);
                }
            }
        }
    }

    private int countMinesAround(int r, int c) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = r + dr;
                int nc = c + dc;
                if (isValid(nr, nc) && cells[nr][nc].isMine()) {
                    count++;
                }
            }
        }
        return count;
    }

    public boolean isValid(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public Cell getCell(int r, int c) {
        if (isValid(r, c)) {
            return cells[r][c];
        }
        return null;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getMineCount() {
        return mineCount;
    }
    
    public List<Cell> getNeighbors(int r, int c) {
        List<Cell> neighbors = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = r + dr;
                int nc = c + dc;
                if (isValid(nr, nc)) {
                    neighbors.add(cells[nr][nc]);
                }
            }
        }
        return neighbors;
    }
}
