package minesweeper.view;

import minesweeper.controller.GameController;
import minesweeper.model.GameModel;
import minesweeper.model.GameState;

import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel implements GameModel.GameModelObserver {
    private final GameModel gameModel;
    private final GameController gameController;
    private final JLabel minesLabel;
    private final JLabel timerLabel;
    private final SmileyButton resetButton;

    public HeaderPanel(GameModel gameModel, GameController gameController) {
        this.gameModel = gameModel;
        this.gameController = gameController;
        this.gameModel.addObserver(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createLoweredBevelBorder());

        minesLabel = new JLabel("000", SwingConstants.CENTER);
        minesLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        minesLabel.setOpaque(true);
        minesLabel.setBackground(Color.BLACK);
        minesLabel.setForeground(Color.RED);
        minesLabel.setPreferredSize(new Dimension(60, 30));

        timerLabel = new JLabel("000", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Monospaced", Font.BOLD, 20));
        timerLabel.setOpaque(true);
        timerLabel.setBackground(Color.BLACK);
        timerLabel.setForeground(Color.RED);
        timerLabel.setPreferredSize(new Dimension(60, 30));

        resetButton = new SmileyButton();
        resetButton.addActionListener(e -> {
            // Reset logic handled by MainWindow via listener on this button
        });

        add(minesLabel, BorderLayout.WEST);
        add(resetButton, BorderLayout.CENTER);
        add(timerLabel, BorderLayout.EAST);
        
        updateView();
    }
    
    public JButton getResetButton() {
        return resetButton;
    }

    private void updateView() {
        int minesLeft = gameModel.getMinesRemaining();
        minesLabel.setText(String.format("%03d", Math.max(0, minesLeft)));
        
        int elapsedSeconds = gameModel.getElapsedSeconds();
        timerLabel.setText(String.format("%03d", elapsedSeconds));
        
        if (gameModel.getState() == GameState.WON) {
            resetButton.setFace(SmileyButton.Face.COOL);
        } else if (gameModel.getState() == GameState.LOST) {
            resetButton.setFace(SmileyButton.Face.DEAD);
        } else {
            resetButton.setFace(SmileyButton.Face.HAPPY);
        }
    }

    @Override
    public void onGameUpdated() {
        updateView();
    }

    @Override
    public void onGameWon() {
        updateView();
    }

    @Override
    public void onGameLost() {
        updateView();
    }
}
