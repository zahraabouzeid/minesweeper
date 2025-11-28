package minesweeper.view;

import minesweeper.controller.GameController;
import minesweeper.model.GameModel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private GameModel gameModel;
    private GameController gameController;
    private BoardPanel boardPanel;
    private HeaderPanel headerPanel;
    private JPanel mainContainer;

    public MainWindow() {
        setTitle("Minesweeper");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        startNewGame(9, 9, 10);
        
        createMenuBar();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startNewGame(int rows, int cols, int mines) {
        gameModel = new GameModel(rows, cols, mines);
        gameController = new GameController(gameModel);
        
        if (mainContainer != null) {
            remove(mainContainer);
        }
        
        mainContainer = new JPanel(new BorderLayout());
        
        headerPanel = new HeaderPanel(gameModel, gameController);
        boardPanel = new BoardPanel(gameModel, gameController);
        
        headerPanel.getResetButton().addActionListener(e -> startNewGame(rows, cols, mines));
        
        mainContainer.add(headerPanel, BorderLayout.NORTH);
        mainContainer.add(boardPanel, BorderLayout.CENTER);
        
        add(mainContainer);
        
        pack();
        setLocationRelativeTo(null);
    }

    private void createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGameItem = new JMenuItem("New");
        newGameItem.addActionListener(e -> startNewGame(9, 9, 10));
        
        JMenu difficultyMenu = new JMenu("Difficulty");
        JMenuItem beginnerItem = new JMenuItem("Beginner");
        beginnerItem.addActionListener(e -> startNewGame(9, 9, 10));
        
        JMenuItem intermediateItem = new JMenuItem("Intermediate");
        intermediateItem.addActionListener(e -> startNewGame(16, 16, 40));
        
        JMenuItem expertItem = new JMenuItem("Expert");
        expertItem.addActionListener(e -> startNewGame(16, 30, 99));
        
        difficultyMenu.add(beginnerItem);
        difficultyMenu.add(intermediateItem);
        difficultyMenu.add(expertItem);
        
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        
        gameMenu.add(newGameItem);
        gameMenu.add(difficultyMenu);
        gameMenu.addSeparator();
        gameMenu.add(exitItem);
        
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(e -> gameController.undo());
        
        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(e -> gameController.redo());
        
        JMenuItem replayItem = new JMenuItem("Replay");
        replayItem.addActionListener(e -> gameController.startReplay());
        
        editMenu.add(undoItem);
        editMenu.add(redoItem);
        editMenu.addSeparator();
        editMenu.add(replayItem);
        
        menuBar.add(gameMenu);
        menuBar.add(editMenu);
        
        setJMenuBar(menuBar);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainWindow::new);
    }
}
