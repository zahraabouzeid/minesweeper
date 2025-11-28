package minesweeper.model;

public interface BoardGenerator {
    void generateMines(Board board, int mineCount, int firstClickRow, int firstClickCol);
}
