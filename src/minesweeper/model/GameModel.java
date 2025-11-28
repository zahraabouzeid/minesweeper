package minesweeper.model;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private final Board board;
    private GameState state;
    private int flagsPlaced;
    private final List<GameModelObserver> observers;
    private int elapsedSeconds;
    private Timer gameTimer;
    private long startTime;
    private boolean timerStarted;
    private boolean isReplaying;

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
        this.elapsedSeconds = 0;
        this.timerStarted = false;
        this.isReplaying = false;
        initTimer();
    }
    
    private void initTimer() {
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timerStarted && state == GameState.PLAYING) {
                    elapsedSeconds = (int) ((System.currentTimeMillis() - startTime) / 1000);
                    if (elapsedSeconds > 999) {
                        elapsedSeconds = 999;
                    }
                    notifyObservers();
                }
            }
        });
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
        if (state == GameState.WON || state == GameState.LOST) {
            stopTimer();
        }
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
    
    public int getElapsedSeconds() {
        if (timerStarted && state == GameState.PLAYING) {
            elapsedSeconds = (int) ((System.currentTimeMillis() - startTime) / 1000);
            if (elapsedSeconds > 999) {
                elapsedSeconds = 999;
            }
        }
        return elapsedSeconds;
    }
    
    public void startTimer() {
        if (!timerStarted && !isReplaying) {
            timerStarted = true;
            startTime = System.currentTimeMillis();
            elapsedSeconds = 0;
            gameTimer.start();
        }
    }
    
    public void setReplaying(boolean replaying) {
        this.isReplaying = replaying;
    }
    
    public void stopTimer() {
        if (timerStarted) {
            gameTimer.stop();
            if (state == GameState.PLAYING) {
                elapsedSeconds = (int) ((System.currentTimeMillis() - startTime) / 1000);
                if (elapsedSeconds > 999) {
                    elapsedSeconds = 999;
                }
            }
        }
    }
    
    public void resetTimer() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        timerStarted = false;
        elapsedSeconds = 0;
        startTime = 0;
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
