package minesweeper.view;

import minesweeper.controller.GameController;
import minesweeper.model.Board;
import minesweeper.model.Cell;
import minesweeper.model.GameModel;
import minesweeper.model.GameState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel implements GameModel.GameModelObserver {
    private final GameModel gameModel;
    private final GameController gameController;
    private final int rows;
    private final int cols;
    private final JButton[][] buttons;

    public BoardPanel(GameModel gameModel, GameController gameController) {
        this.gameModel = gameModel;
        this.gameController = gameController;
        this.rows = gameModel.getBoard().getRows();
        this.cols = gameModel.getBoard().getCols();
        this.buttons = new JButton[rows][cols];
        
        this.gameModel.addObserver(this);

        setLayout(new GridLayout(rows, cols));
        initializeButtons();
    }

    private void initializeButtons() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                JButton btn = new JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        int row = -1;
                        int col = -1;
                        Integer rProp = (Integer) getClientProperty("row");
                        Integer cProp = (Integer) getClientProperty("col");
                        if (rProp != null) {
                            row = rProp;
                            col = cProp;
                            
                            Cell cell = gameModel.getBoard().getCell(row, col);
                            if (cell.isRevealed() && cell.isMine()) {
                                drawMine(g, getWidth(), getHeight());
                            } else if (!cell.isRevealed() && cell.isFlagged()) {
                                drawFlag(g, getWidth(), getHeight());
                            }
                        }
                    }
                };
                btn.setPreferredSize(new Dimension(25, 25));
                btn.setMargin(new Insets(0, 0, 0, 0));
                btn.setFont(new Font("Arial", Font.BOLD, 12));
                btn.putClientProperty("row", r);
                btn.putClientProperty("col", c);
                
                btn.setBorder(BorderFactory.createRaisedBevelBorder());
                btn.setBackground(new Color(192, 192, 192));
                btn.setOpaque(true);
                
                final int row = r;
                final int col = c;
                
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameModel.getState() != GameState.PLAYING) return;
                        
                        if (SwingUtilities.isRightMouseButton(e)) {
                            gameController.rightClick(row, col);
                        } else if (SwingUtilities.isLeftMouseButton(e)) {
                            gameController.leftClick(row, col);
                        }
                    }
                });
                
                buttons[r][c] = btn;
                add(btn);
            }
        }
    }

    private void drawMine(Graphics g, int w, int h) {
        g.setColor(Color.BLACK);
        g.fillOval(4, 4, w - 8, h - 8);
        g.drawLine(w/2, 2, w/2, h-2);
        g.drawLine(2, h/2, w-2, h/2);
        g.drawLine(4, 4, w-4, h-4);
        g.drawLine(4, h-4, w-4, 4);
        g.setColor(Color.WHITE);
        g.fillOval(w/3, h/3, 3, 3);
    }

    private void drawFlag(Graphics g, int w, int h) {
        g.setColor(Color.RED);
        int[] xPoints = {w/4, w/2 + 4, w/4};
        int[] yPoints = {4, 8, 12};
        g.fillPolygon(xPoints, yPoints, 3);
        g.setColor(Color.BLACK);
        g.drawLine(w/4, 4, w/4, h-4);
        g.fillRect(w/4 - 2, h-6, 6, 2);
    }

    private void updateBoard() {
        Board board = gameModel.getBoard();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Cell cell = board.getCell(r, c);
                JButton btn = buttons[r][c];
                
                if (cell.isRevealed()) {
                    btn.setBackground(Color.LIGHT_GRAY);
                    btn.setBorder(BorderFactory.createLoweredBevelBorder());
                    if (cell.isMine()) {
                        btn.setText("");
                        btn.setBackground(Color.RED);
                    } else {
                        int count = cell.getNeighborMineCount();
                        btn.setText(count > 0 ? String.valueOf(count) : "");
                        setNumberColor(btn, count);
                    }
                } else {
                    btn.setEnabled(true);
                    btn.setBackground(new Color(192, 192, 192));
                    btn.setBorder(BorderFactory.createRaisedBevelBorder());
                    btn.setText("");
                }
                btn.repaint();
            }
        }
    }
    
    private void setNumberColor(JButton btn, int count) {
        Color color;
        switch (count) {
            case 1: color = Color.BLUE; break;
            case 2: color = new Color(0, 128, 0); break;
            case 3: color = Color.RED; break;
            case 4: color = new Color(0, 0, 128); break;
            case 5: color = new Color(128, 0, 0); break;
            case 6: color = new Color(0, 128, 128); break;
            case 7: color = Color.BLACK; break;
            case 8: color = Color.GRAY; break;
            default: color = Color.BLACK;
        }
        btn.setText("<html><b style='font-family:SansSerif; font-size:18px; color:" + toHexString(color) + "'>" + (count > 0 ? count : "") + "</b></html>");
    }
    
    private String toHexString(Color c) {
        return String.format("#%02x%02x%02x", c.getRed(), c.getGreen(), c.getBlue());
    }

    @Override
    public void onGameUpdated() {
        updateBoard();
    }

    @Override
    public void onGameWon() {
        updateBoard();
        if (gameController.isReplaying()) return;
        
        int choice = JOptionPane.showConfirmDialog(this, "You Won! Do you want to see a replay?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            gameController.startReplay();
        }
    }

    @Override
    public void onGameLost() {
        updateBoard();
        if (gameController.isReplaying()) return;

        int choice = JOptionPane.showConfirmDialog(this, "Game Over! Do you want to see a replay?", "Game Over", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            gameController.startReplay();
        }
    }
}
