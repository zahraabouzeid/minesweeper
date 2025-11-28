package minesweeper.model;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private final Board board;
    private GameState state;
    private int flagsPlaced;
    private final List<GameModelObserver> observers;

    public interface GameModelObserver {
        void onGameUpdated();
        void onGameWon();
        void onGameLost();
    }

    public GameModel(int rows, int cols, int mineCount) {
        this.board = new Board(rows, cols, mineCount, new RandomBoardGenerator());
        this.state = GameState.PLAYING;
        this.flagsPlaced = 0;
        this.observers = new ArrayList<>();
    }

    public void addObserver(GameModelObserver observer) {
        observers.add(observer);
    }

    public Board getBoard() {
        return board;
    }

    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
        notifyObservers();
        if (state == GameState.WON) {
            for (GameModelObserver o : observers) o.onGameWon();
        } else if (state == GameState.LOST) {
            for (GameModelObserver o : observers) o.onGameLost();
        }
    }

    public int getFlagsPlaced() {
        return flagsPlaced;
    }

    public void setFlagsPlaced(int flagsPlaced) {
        this.flagsPlaced = flagsPlaced;
        notifyObservers();
    }

    public int getMinesRemaining() {
        return board.getMineCount() - flagsPlaced;
    }

    public void notifyObservers() {
        for (GameModelObserver o : observers) {
            o.onGameUpdated();
        }
    }

    public void checkWinCondition() {
        if (state != GameState.PLAYING) return;

        int nonMineCells = board.getRows() * board.getCols() - board.getMineCount();
        int revealedCells = 0;

        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                if (board.getCell(r, c).isRevealed()) {
                    revealedCells++;
                }
            }
        }

        if (revealedCells == nonMineCells) {
            setState(GameState.WON);
        }
    }
}
