package challenge;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Options extends JPanel {
    private static final String[] desc = {
        "left",
        "right",
        "soft",
        "hard",
        "ccw",
        "cw",
        "180",
        "hold"
    };
    public static int keysPressed = 0;
    public static boolean hasBeenSet = true;
    public static int[] control = new int[8];

    public Options() {
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                control[keysPressed] = e.getKeyCode();
                keysPressed++;
                if (keysPressed == 8) {
                    hasBeenSet = true;
                    keysPressed = 0;
                    Main.frame.setContentPane(new Menu());
                    Main.frame.pack();
                } else {
                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 700, 700);
        g.setColor(Color.BLACK);
        g.drawString(desc[keysPressed], 300, 300);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700, 700);
    }
}
