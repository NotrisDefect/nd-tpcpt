import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JPanel {

    public static final Main instance = new Main();
    public static final boolean[] onlyOnePress = {false, false, false, true, true, true, true, true};
    public static final int PIECESIZE = 32;
    public static final int VISIBLEROWS = 21;
    public static final int GRIDSIZE = 1;
    public static final int TLCX = 190;
    public static final int TLCY = -625;
    public static final Point TLCS = new Point(TLCX, TLCY);
    public static final Point TLCH = new Point(TLCX - (PIECESIZE + GRIDSIZE) * 5, TLCY + (20) * (PIECESIZE + GRIDSIZE));
    public static final Point TLCN = new Point(TLCX + (PIECESIZE + GRIDSIZE) * 11, TLCY + (20) * (PIECESIZE + GRIDSIZE));

    public final String play = "Press P to play";
    public final String pause = "Press P to pause";
    public final String resume = "Press P to resume";
    public final String options = "Press O for options";
    public final String retry = "Press R to retry";
    public final String back = "Press B to go back";
    public final String[] kontrols = {"Left", "Right", "Soft drop", "Hard drop", "CCW", "CW", "180", "Hold"};
    public final int MAINMENU = 0;
    public final int OPTIONS = 1;
    public final int GAME = 2;
    public final int GAMEOVER = 3;
    public final JFrame frame = new JFrame("Notris Defect");
    public final Table table = new Table();
    public int[] controls = new int[8];
    public boolean[] keyAlreadyProcessed = new boolean[controls.length];
    public boolean[] keyIsDown = new boolean[controls.length];
    public int[] howLongIsPressed = new int[controls.length];
    public int menuOpen;
    public int keysPressed = 0;
    public boolean hasBeenSet = false;
    public boolean paused = false;
    public KeyListener[] keyListeners = {
        new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    table.extStartGame();
                    openMenu(GAME);
                } else if (e.getKeyCode() == KeyEvent.VK_O) {
                    openMenu(OPTIONS);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        },
        new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                controls[keysPressed] = e.getKeyCode();
                keysPressed++;
                if (keysPressed == 8) {
                    hasBeenSet = true;
                    keysPressed = 0;
                    openMenu(MAINMENU);
                } else {
                    repaint();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        },
        new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    paused ^= true;
                } else if (e.getKeyCode() == KeyEvent.VK_B) {
                    table.extAbortGame();
                    openMenu(MAINMENU);
                }

                int key = e.getKeyCode();
                for (int i = 0; i < controls.length; i++) {
                    if (key == controls[i]) {
                        keyIsDown[i] = true;
                        break;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                for (int i = 0; i < controls.length; i++) {
                    if (key == controls[i]) {
                        keyIsDown[i] = false;
                        keyAlreadyProcessed[i] = false;
                        howLongIsPressed[i] = 0;
                        break;
                    }
                }
            }
        },
        new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    table.extStartGame();
                    openMenu(GAME);
                } else if (e.getKeyCode() == KeyEvent.VK_B) {
                    openMenu(MAINMENU);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        }
    };

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

    public static void main(String[] args) {
        Main.instance.init();
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

    public void drawPix(Graphics g, Point tlc, int x, int y) {
        g.fillRect(tlc.x + x * (PIECESIZE + GRIDSIZE), tlc.y + y * (PIECESIZE + GRIDSIZE), PIECESIZE, PIECESIZE);
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

    public void init() {
        openMenu(MAINMENU);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 700);
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);
        frame.setContentPane(instance);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocation(x, y);
    }

    public void openMenu(int n) {
        frame.removeKeyListener(keyListeners[menuOpen]);
        menuOpen = n;
        frame.addKeyListener(keyListeners[n]);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 700, 700);
        switch (menuOpen) {
            case MAINMENU:
                paintComponentMAINMENU(g);
                break;
            case OPTIONS:
                paintComponentOPTIONS(g);
                break;
            case GAME:
                paintComponentGAME(g);
                break;
            case GAMEOVER:
                paintComponentGAMEOVER(g);
                break;
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(690, 690);
    }

    public void paintComponentGAME(Graphics g) {
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
        if (table.isDead()) {
            openMenu(GAMEOVER);
        }
    }

    public void paintComponentGAMEOVER(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("game over component", 300 + (int) (Math.random() * 5), 300);
    }

    public void paintComponentMAINMENU(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("main menu component", 300 + (int) (Math.random() * 5), 300);
    }

    public void paintComponentOPTIONS(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString(kontrols[keysPressed], 300, 300);
    }
}
