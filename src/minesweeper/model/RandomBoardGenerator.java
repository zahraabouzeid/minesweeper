package minesweeper.model;

import java.util.Random;

public class RandomBoardGenerator implements BoardGenerator {
    @Override
    public void generateMines(Board board, int mineCount, int firstClickRow, int firstClickCol) {
        int rows = board.getRows();
        int cols = board.getCols();
        int minesPlaced = 0;
        Random random = new Random();

        while (minesPlaced < mineCount) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);

            // Don't place mine on the first clicked cell or its neighbors (safe start)
            if (Math.abs(r - firstClickRow) <= 1 && Math.abs(c - firstClickCol) <= 1) {
                continue;
            }

            if (!board.getCell(r, c).isMine()) {
                board.getCell(r, c).setMine(true);
                minesPlaced++;
            }
        }
    }
}
