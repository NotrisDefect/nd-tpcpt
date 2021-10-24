package challenge;

import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends JPanel implements ActionListener {

    public static final Table table = new Table();
    public static final boolean[] onlyOnePress = {false, false, false, true, true, true, true, true};
    private static final int PIECESIZE = 32;
    private static final int VISIBLEROWS = 21;
    private static final int GRIDSIZE = 1;
    private static final int TLCX = 190;
    private static final int TLCY = -625;
    private static final Point TLCS = new Point(TLCX, TLCY);
    private static final Point TLCH = new Point(TLCX - (PIECESIZE + GRIDSIZE) * 5, TLCY + (table.STAGESIZEY / 2) * (PIECESIZE + GRIDSIZE));
    private static final Point TLCN = new Point(TLCX + (PIECESIZE + GRIDSIZE) * 11, TLCY + (table.STAGESIZEY / 2) * (PIECESIZE + GRIDSIZE));
    public static String menuS = "menu";
    public static String pauseS = "pause";
    public static boolean paused = false;
    public static int[] keys = Options.control;
    public static boolean[] keyAlreadyProcessed = new boolean[keys.length];
    public static boolean[] keyIsDown = new boolean[keys.length];
    public static int[] howLongIsPressed = new int[keys.length];
    public JButton menu;
    public JButton pause;


    public Game() {
        setLayout(null);

        menu = new JButton(menuS);
        menu.setActionCommand(menuS);
        menu.addActionListener(this);
        menu.setBounds(0, 620, 80, 40);
        add(menu);

        pause = new JButton(pauseS);
        pause.setActionCommand(pauseS);
        pause.addActionListener(this);
        pause.setBounds(0, 660, 80, 40);
        add(pause);

        table.extStartGame();

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                for (int i = 0; i < keys.length; i++) {
                    if (key == keys[i]) {
                        keyIsDown[i] = true;
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                for (int i = 0; i < keys.length; i++) {
                    if (key == keys[i]) {
                        keyIsDown[i] = false;
                        keyAlreadyProcessed[i] = false;
                        Game.howLongIsPressed[i] = 0;
                        break;
                    }
                }
            }
        });
    }

    public static Color intToColor(int number) {
        switch (number) {
            case 0:
                return Color.RED;
            case 1:
                return Color.ORANGE;
            case 2:
                return Color.YELLOW;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.CYAN;
            case 5:
                return Color.BLUE;
            case 6:
                return Color.MAGENTA;
            case 7:
                return Color.BLACK;
        }
        return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(pauseS)) {
            if (!paused) {
                pause.setText("resume");
                paused = true;
            } else {
                pause.setText(pauseS);
                paused = false;
            }
        } else if (e.getActionCommand().equals(menuS)) {
            table.extAbortGame();
            Main.frame.setContentPane(new Menu());
            Main.frame.pack();
        }
    }

    public void drawCurrent(Graphics g) {
        g.setColor(intToColor(table.getCurrentPiece().getColor()));
        for (Point point : table.getPoints(table.getCurrentPiece().getColor(), table.getCurrentPiece().getRotation())) {
            drawPix(g, TLCS, point.x + table.getCurrentPiece().getX(), point.y + table.getCurrentPiece().getY());
        }
    }

    public void drawHold(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(TLCH.x, TLCH.y, (PIECESIZE + GRIDSIZE) * 4, (PIECESIZE + GRIDSIZE) * 4);
        if (table.getHeldPiece() != null) {
            g.setColor(intToColor(table.getHeldPiece().getColor()));
            for (Point point : table.getPoints(table.getHeldPiece().getColor(), 0)) {
                drawPix(g, TLCH, point.x, point.y);
            }
        }
    }

    public void drawNext(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(TLCN.x, TLCN.y, (PIECESIZE + GRIDSIZE) * 4, (PIECESIZE + GRIDSIZE) * 4 * 5);
        for (int i = 0; i < 5; i++) {
            int piece = table.getNextPieces()[i].getColor();
            g.setColor(intToColor(piece));
            for (Point point : table.getPoints(piece, 0)) {
                drawPix(g, TLCN, point.x, i * 4 + point.y);
            }
        }
    }

    public void drawStage(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(TLCS.x - 7, TLCS.y - 7 + 625, 10 * 33 + 14, 21 * 33 + 14);
        for (int i = table.STAGESIZEY - VISIBLEROWS; i < table.STAGESIZEY; i++) {
            for (int j = 0; j < table.STAGESIZEX; j++) {
                g.setColor(intToColor(table.getStage()[i][j]));
                drawPix(g, TLCS, j, i);
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 700, 700);
        long timeStart = System.nanoTime();

        g.setColor(Color.WHITE);
        g.drawString("Score: " + table.getTotalScore(), 30, 30);

        drawStage(g);
        drawHold(g);
        drawNext(g);
        drawCurrent(g);

        long timeEnd = System.nanoTime();
        long timeElapsed = timeEnd - timeStart;
        g.setColor(Color.YELLOW);
        g.drawString(1000000000 / timeElapsed + " FPS", 10, 10);
        if (!table.isDead()) {
            repaint();
        } else {
            g.setColor(Color.WHITE);
            g.drawString("GAME OVER", 30, 500);
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(700, 700);
    }

    private void drawPix(Graphics g, Point tlc, int x, int y) {
        g.fillRect(tlc.x + x * (PIECESIZE + GRIDSIZE), tlc.y + y * (PIECESIZE + GRIDSIZE), PIECESIZE, PIECESIZE);
    }

}
