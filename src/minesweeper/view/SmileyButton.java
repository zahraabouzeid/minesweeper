package minesweeper.view;

import javax.swing.*;
import java.awt.*;

public class SmileyButton extends JButton {
    public enum Face {
        HAPPY,
        COOL,
        DEAD 
    }

    private Face currentFace;

    public SmileyButton() {
        this.currentFace = Face.HAPPY;
        setPreferredSize(new Dimension(30, 30));
        setBorder(BorderFactory.createRaisedBevelBorder());
        setContentAreaFilled(false);
    }

    public void setFace(Face face) {
        this.currentFace = face;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth();
        int h = getHeight();
        
        int diameter = Math.min(w, h) - 12;
        int x = (w - diameter) / 2;
        int y = (h - diameter) / 2;

        // Background
        g2.setColor(Color.YELLOW);
        g2.fillOval(x, y, diameter, diameter);
        g2.setColor(Color.BLACK);
        g2.drawOval(x, y, diameter, diameter);

        // Eyes
        int eyeSize = diameter / 8;
        int eyeY = y + diameter / 3;
        int leftEyeX = x + diameter / 3;
        int rightEyeX = x + 2 * diameter / 3 - eyeSize;

        if (currentFace == Face.DEAD) {
            g2.drawLine(leftEyeX, eyeY, leftEyeX + eyeSize, eyeY + eyeSize);
            g2.drawLine(leftEyeX + eyeSize, eyeY, leftEyeX, eyeY + eyeSize);
            g2.drawLine(rightEyeX, eyeY, rightEyeX + eyeSize, eyeY + eyeSize);
            g2.drawLine(rightEyeX + eyeSize, eyeY, rightEyeX, eyeY + eyeSize);
        } else if (currentFace == Face.COOL) {
            g2.setColor(Color.BLACK);
            g2.fillOval(leftEyeX - 2, eyeY, diameter / 2, eyeSize * 2); // Sunglasses
            g2.drawLine(leftEyeX - 2, eyeY, rightEyeX + eyeSize + 2, eyeY);
        } else {
            g2.fillOval(leftEyeX, eyeY, eyeSize, eyeSize);
            g2.fillOval(rightEyeX, eyeY, eyeSize, eyeSize);
        }

        // Mouth
        g2.setColor(Color.BLACK);
        int mouthY = y + diameter / 2;
        int mouthW = diameter / 2;
        int mouthX = x + (diameter - mouthW) / 2;
        
        if (currentFace == Face.HAPPY) {
            g2.drawArc(mouthX, mouthY, mouthW, diameter / 3, 0, -180);
        } else if (currentFace == Face.DEAD) {
            g2.drawArc(mouthX, mouthY + diameter/6, mouthW, diameter / 3, 0, 180); // Frown
        } else if (currentFace == Face.COOL) {
             g2.drawArc(mouthX, mouthY, mouthW, diameter / 4, 0, -180); // Smirk
        }
    }
}
