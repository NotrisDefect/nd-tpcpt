import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class a extends JPanel {
    public static final int PIECE_T = 6;
    public static final int PIECE_NONE = 7;
    public static final int SPIN_NONE = 0;
    public static final int SPIN_MINI = 1;
    public static final int SPIN_FULL = 2;
    public static final int TPS = 100;
    public static final int ALL_DELAY_MS = 100;
    public static final int LINE_DELAY_MS = 500;
    public static final int ARR_MS = 20;
    public static final int DAS_MS = 160;
    public static final int MANIPS = 15;
    public static final Point[][][] PIECEMATRIX = new Point[][][]{
        {
            {new Point(0, 0), new Point(1, 0), new Point(1, 1), new Point(2, 1)},
            {new Point(2, 0), new Point(2, 1), new Point(1, 1), new Point(1, 2)},
            {new Point(2, 2), new Point(1, 2), new Point(1, 1), new Point(0, 1)},
            {new Point(0, 2), new Point(0, 1), new Point(1, 1), new Point(1, 0)}
        },
        {
            {new Point(2, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
            {new Point(1, 0), new Point(1, 2), new Point(1, 1), new Point(2, 2)},
            {new Point(0, 2), new Point(2, 1), new Point(1, 1), new Point(0, 1)},
            {new Point(1, 2), new Point(1, 0), new Point(1, 1), new Point(0, 0)}
        },
        {
            {new Point(1, 0), new Point(2, 0), new Point(1, 1), new Point(2, 1)},
            {new Point(1, 0), new Point(1, 1), new Point(2, 0), new Point(2, 1)},
            {new Point(2, 1), new Point(1, 1), new Point(2, 0), new Point(1, 0)},
            {new Point(2, 1), new Point(2, 0), new Point(1, 1), new Point(1, 0)},
        },
        {
            {new Point(1, 0), new Point(2, 0), new Point(0, 1), new Point(1, 1)},
            {new Point(1, 0), new Point(2, 1), new Point(1, 1), new Point(2, 2)},
            {new Point(1, 2), new Point(0, 2), new Point(2, 1), new Point(1, 1)},
            {new Point(1, 2), new Point(0, 1), new Point(1, 1), new Point(0, 0)}
        },
        {
            {new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1)},
            {new Point(2, 0), new Point(2, 2), new Point(2, 1), new Point(2, 3)},
            {new Point(3, 2), new Point(2, 2), new Point(1, 2), new Point(0, 2)},
            {new Point(1, 3), new Point(1, 2), new Point(1, 1), new Point(1, 0)}
        },
        {
            {new Point(0, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
            {new Point(1, 0), new Point(1, 1), new Point(2, 0), new Point(1, 2)},
            {new Point(2, 2), new Point(2, 1), new Point(1, 1), new Point(0, 1)},
            {new Point(0, 2), new Point(1, 2), new Point(1, 1), new Point(1, 0)}
        },
        {
            {new Point(1, 0), new Point(0, 1), new Point(1, 1), new Point(2, 1)},
            {new Point(1, 0), new Point(2, 1), new Point(1, 1), new Point(1, 2)},
            {new Point(1, 2), new Point(2, 1), new Point(1, 1), new Point(0, 1)},
            {new Point(1, 2), new Point(0, 1), new Point(1, 1), new Point(1, 0)}
        }
    };
    public static final int[][] STATES = {
        {8, 0, 8, 7},
        {1, 8, 2, 8},
        {8, 3, 8, 4},
        {6, 8, 5, 8}
    };
    public static final int[][] SCORE = {
        {0, 100, 400},
        {100, 200, 800},
        {300, 400, 1200},
        {500, 0, 1600},
        {800, 0, 2600}
    };
    public static final int SCORE_ALLCLEAR = 3500;
    public static final int SCORE_COMBO = 50;
    public static final int SCORE_SOFTDROP = 1;
    public static final int SCORE_HARDDROP = 2;
    public static final int BAG_SIZE = 7;
    public static final int PIECEPOINTS = 4;
    public static final double gravityMulti = 20;
    public static final a instance = new a();
    public static final boolean[] onlyOnePress = {false, false, false, true, true, true, true, true};
    public static final int PIECESIZE = 32;
    public static final int VISIBLEROWS = 21;
    public static final int GRIDSIZE = 1;
    public static final int TLCX = 190;
    public static final int TLCY = -625;
    public static final Point TLCS = new Point(TLCX, TLCY);
    public static final Point TLCH = new Point(TLCX - (PIECESIZE + GRIDSIZE) * 5, TLCY + (20) * (PIECESIZE + GRIDSIZE));
    public static final Point TLCN = new Point(TLCX + (PIECESIZE + GRIDSIZE) * 11, TLCY + (20) * (PIECESIZE + GRIDSIZE));
    public final String[] kontrols = {"Left", "Right", "Soft drop", "Hard drop", "CCW", "CW", "180", "Hold"};
    public final int MAINMENU = 0;
    public final int OPTIONS = 1;
    public final int GAME = 2;
    public final int GAMEOVER = 3;
    public final JFrame frame = new JFrame("Notris Defect");
    public final Point[][][] KICKTABLE = new Point[][][]{
        {
            {new Point(0, 0), new Point(-1, 0), new Point(-1, +1), new Point(0, -2), new Point(-1, -2)},
            {new Point(0, 0), new Point(+1, 0), new Point(+1, -1), new Point(0, +2), new Point(+1, +2)},

            {new Point(0, 0), new Point(+1, 0), new Point(+1, -1), new Point(0, +2), new Point(+1, +2)},
            {new Point(0, 0), new Point(-1, 0), new Point(-1, +1), new Point(0, -2), new Point(-1, -2)},

            {new Point(0, 0), new Point(+1, 0), new Point(+1, +1), new Point(0, -2), new Point(+1, -2)},
            {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(0, +2), new Point(-1, +2)},

            {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(0, +2), new Point(-1, +2)},
            {new Point(0, 0), new Point(+1, 0), new Point(+1, +1), new Point(0, -2), new Point(+1, -2)},

            {new Point(0, 0)}
        },
        {
            {new Point(0, 0), new Point(-2, 0), new Point(+1, 0), new Point(-2, -1), new Point(+1, +2)},
            {new Point(0, 0), new Point(+2, 0), new Point(-1, 0), new Point(+2, +1), new Point(-1, -2)},

            {new Point(0, 0), new Point(-1, 0), new Point(+2, 0), new Point(-1, +2), new Point(+2, -1)},
            {new Point(0, 0), new Point(+1, 0), new Point(-2, 0), new Point(+1, -2), new Point(-2, +1)},

            {new Point(0, 0), new Point(+2, 0), new Point(-1, 0), new Point(+2, +1), new Point(-1, -2)},
            {new Point(0, 0), new Point(-2, 0), new Point(+1, 0), new Point(-2, -1), new Point(+1, +2)},

            {new Point(0, 0), new Point(+1, 0), new Point(-2, 0), new Point(+1, -2), new Point(-2, +1)},
            {new Point(0, 0), new Point(-1, 0), new Point(+2, 0), new Point(-1, +2), new Point(+2, -1)},

            {new Point(0, 0)}
        }
    };
    public final double lockDelay = 0.5d;
    public int[] controls = new int[8];
    public boolean[] keyAlreadyProcessed = new boolean[controls.length];
    public boolean[] keyIsDown = new boolean[controls.length];
    public int[] howLongIsPressed = new int[controls.length];
    public int menuOpen;
    public int keysPressed = 0;
    public boolean hasBeenSet = false;
    public boolean paused = false;
    public Random pieceRandomizer;
    public boolean dead = true;
    public int[][] stage;
    public Piece current;
    public Piece[] nextPieces;
    public int nextPiecesLeft;
    public Piece heldPiece;
    public boolean held;
    public double counter;
    public double limit;
    public int combo;
    public int backToBack;
    public long ticksPassed;
    public long totalScore;
    public long totalLinesCleared;
    public long totalPiecesPlaced;
    public int level;
    public double gravity;
    public int lowestPossiblePosition;
    public int spinState;
    public int waitForClearTicks = 0;
    public int STAGESIZEX = 10;
    public int STAGESIZEY = 40;
    public final int[] MANIPS_USED = new int[STAGESIZEY];
    public int PLAYABLEROWS = 20;
    public int NEXTPIECESMAX = 5;

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
            default:
                return null;
        }
    }

    public static void main(String[] args) {
        a.instance.init();
    }

    public void calcCurrentPieceLowestPossiblePosition() {
        int result = current.getY();
        while (!isColliding(current.getX(), result + 1, current.getRotation())) {
            result++;
        }
        lowestPossiblePosition = result;
    }

    public void calcGravity() {
        gravity = Math.pow(0.8 - (level - 1) * 0.007, level - 1);
    }

    public void calcLimit() {
        limit = isTouchingGround() ? lockDelay : (a.instance.keyIsDown[2] ? gravity / gravityMulti : gravity);
    }

    public void checkLockOut() {
        Point[] piece = PIECEMATRIX[current.getColor()][current.getRotation()];
        for (int i = 0; i < PIECEPOINTS; i++) {
            Point point = piece[i];
            if (stage[point.y + current.getY()][point.x + current.getX()] != PIECE_NONE) {
                dead = true;
            }
        }
    }

    public void checkSpin(int tries, int oldRotation) {
        int x = current.getX();
        int y = current.getY();
        int rot = current.getRotation();
        boolean[] corners = new boolean[4];
        boolean[] isSignificant = {true, true, false, false};

        if (current.getColor() != PIECE_T) {
            return;
        }
        if (tries == 4 && ((oldRotation == 0 || oldRotation == 2) && (rot == 1 || rot == 3))) {
            spinState = SPIN_FULL;
            return;
        }

        corners[0] = isSolid(x, y);
        corners[1] = isSolid(x + 2, y);
        corners[2] = isSolid(x + 2, y + 2);
        corners[3] = isSolid(x, y + 2);

        int significant = 0;
        int others = 0;
        for (int i = 0; i < 4; i++) {
            if (corners[i]) {
                if (isSignificant[(rot + i) % 4]) {
                    significant++;
                } else {
                    others++;
                }
            }
        }

        if (significant == 2 && others >= 1) {
            spinState = SPIN_FULL;
        } else if (significant == 1 && others == 2) {
            spinState = SPIN_MINI;
        } else {
            spinState = SPIN_NONE;
        }
    }

    public void checkTopOut() {
        Point[] piece = PIECEMATRIX[current.getColor()][current.getRotation()];
        for (int i = 0; i < PIECEPOINTS; i++) {
            Point point = piece[i];
            if (current.getY() + point.y >= STAGESIZEY - PLAYABLEROWS) {
                return;
            }
        }
        dead = true;
    }

    public void clearLine(int line) {
        for (int i = line; i > 0; i--) {
            System.arraycopy(stage[i - 1], 0, stage[i], 0, STAGESIZEX);
        }

        for (int j = 0; j < STAGESIZEX; j++) {
            stage[0][j] = PIECE_NONE;
        }

        totalLinesCleared++;

    }

    public int clearLines() {
        int numClears = 0;
        boolean yes;
        for (int i = STAGESIZEY - 1; i > 0; i--) {
            yes = true;
            for (int j = 0; j < STAGESIZEX; j++) {
                if (stage[i][j] == PIECE_NONE) {
                    yes = false;
                    break;
                }
            }
            if (yes) {
                clearLine(i);
                i++;
                numClears++;
            }
        }

        return numClears;
    }

    public void doAction(int i) {
        switch (i) {
            case 0:
                movePieceRelative(-1, 0);
                break;
            case 1:
                movePieceRelative(1, 0);
                break;
            case 2:
                extDropPieceSoft();
                break;
            case 3:
                hardDropPiece();
                break;
            case 4:
                rotatePiece(-1);
                break;
            case 5:
                rotatePiece(1);
                break;
            case 6:
                rotatePiece(2);
                break;
            case 7:
                holdPiece();
                break;
        }
    }

    public void drawCurrent(Graphics g) {
        g.setColor(intToColor(current.getColor()));
        for (Point point : PIECEMATRIX[current.getColor()][current.getRotation()]) {
            drawPix(g, TLCS, point.x + current.getX(), point.y + current.getY());
        }
    }

    public void drawHold(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(TLCH.x, TLCH.y, (PIECESIZE + GRIDSIZE) * 4, (PIECESIZE + GRIDSIZE) * 4);
        if (heldPiece != null) {
            g.setColor(intToColor(heldPiece.getColor()));
            for (Point point : PIECEMATRIX[heldPiece.getColor()][0]) {
                drawPix(g, TLCH, point.x, point.y);
            }
        }
    }

    public void drawNext(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(TLCN.x, TLCN.y, (PIECESIZE + GRIDSIZE) * 4, (PIECESIZE + GRIDSIZE) * 4 * 5);
        for (int i = 0; i < 5; i++) {
            int piece = nextPieces[i].getColor();
            g.setColor(intToColor(piece));
            for (Point point : PIECEMATRIX[piece][0]) {
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
        for (int i = STAGESIZEY - VISIBLEROWS; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                g.setColor(intToColor(stage[i][j]));
                drawPix(g, TLCS, j, i);
            }
        }
    }

    public void extDropPieceSoft() {
        movePieceRelative(0, 1);
        totalScore += Math.max(0, SCORE_SOFTDROP);
    }

    public void hardDropPiece() {
        int lines = 0;
        while (!isColliding(current.getX(), current.getY() + lines + 1, current.getRotation())) {
            lines++;
        }
        if (lines > 0) {
            movePieceRelative(0, +lines);
            totalScore += (long) lines * SCORE_HARDDROP;
        }
        lockPiece();
    }

    public void holdPiece() {
        if (!held) {
            if (heldPiece == null) {
                heldPiece = new Piece(current);
                makeNextPiece();
            } else {
                Piece temp = new Piece(current);
                current = heldPiece;
                heldPiece = temp;
                calcCurrentPieceLowestPossiblePosition();
                counter = 0;
                calcLimit();
                checkLockOut();
            }
            held = true;
        }
    }

    public void init() {
        menuOpen = MAINMENU;
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
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (menuOpen) {
                    case MAINMENU:
                        keyPressedMAINMENU(e);
                        break;
                    case OPTIONS:
                        keyPressedOPTIONS(e);
                        break;
                    case GAME:
                        keyPressedGAME(e);
                        break;
                    case GAMEOVER:
                        keyPressedGAMEOVER(e);
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (menuOpen) {
                    case GAME:
                        keyReleasedGAME(e);
                        break;
                }
            }
        });
    }

    public void initGame() {
        pieceRandomizer = new Random();
        dead = false;
        a.instance.paused = false;
        stage = new int[STAGESIZEY][STAGESIZEX];
        for (int i = 0; i < STAGESIZEY; i++) {
            for (int j = 0; j < STAGESIZEX; j++) {
                stage[i][j] = PIECE_NONE;
            }
        }

        nextPieces = new Piece[NEXTPIECESMAX + BAG_SIZE];
        nextPiecesLeft = 0;

        heldPiece = null;
        held = false;

        counter = 0;

        combo = -1;
        backToBack = -1;

        ticksPassed = 0;
        totalScore = 0;
        totalLinesCleared = 0;
        totalPiecesPlaced = 0;

        level = 1;

        calcGravity();
        makeNextPiece();
        roomLoop();
    }

    public boolean isColliding(int x, int y, int rotation) {
        Point[] temp;

        temp = PIECEMATRIX[current.getColor()][rotation];
        for (int i = 0; i < PIECEPOINTS; i++) {
            Point point = temp[i];
            if (isInsideBounds(x + point.x, y + point.y)) {
                if (stage[point.y + y][point.x + x] != PIECE_NONE) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean isInsideBounds(int x, int y) {
        return y >= 0 && STAGESIZEY > y && x >= 0 && STAGESIZEX > x;
    }

    public boolean isSolid(int x, int y) {
        return !isInsideBounds(x, y) || stage[y][x] != PIECE_NONE;
    }

    public boolean isTouchingGround() {
        return isColliding(current.getX(), current.getY() + 1, current.getRotation());
    }

    public void keyPressedGAME(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            paused ^= true;
        } else if (e.getKeyCode() == KeyEvent.VK_B) {
            dead = true;
            menuOpen = MAINMENU;
        }

        int key = e.getKeyCode();
        for (int i = 0; i < controls.length; i++) {
            if (key == controls[i]) {
                keyIsDown[i] = true;
                break;
            }
        }
    }

    public void keyPressedGAMEOVER(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_R) {
            initGame();
            menuOpen = GAME;
        } else if (e.getKeyCode() == KeyEvent.VK_B) {
            menuOpen = MAINMENU;
        }
    }

    public void keyPressedMAINMENU(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_P) {
            initGame();
            menuOpen = GAME;
        } else if (e.getKeyCode() == KeyEvent.VK_O) {
            menuOpen = OPTIONS;
        }
    }

    public void keyPressedOPTIONS(KeyEvent e) {
        controls[keysPressed] = e.getKeyCode();
        keysPressed++;
        if (keysPressed == 8) {
            hasBeenSet = true;
            keysPressed = 0;
            menuOpen = MAINMENU;
        } else {
            repaint();
        }
    }

    public void keyReleasedGAME(KeyEvent e) {
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

    public void lockPiece() {
        Point[] temp;

        totalPiecesPlaced++;

        temp = PIECEMATRIX[current.getColor()][current.getRotation()];
        for (int i = 0; i < PIECEPOINTS; i++) {
            Point p = temp[i];
            stage[current.getY() + p.y][current.getX() + p.x] = current.getColor();
        }

        checkTopOut();

        int linesCleared = clearLines();

        if (linesCleared > 0) {
            combo++;

            if (totalLinesCleared * STAGESIZEX == totalPiecesPlaced * PIECEPOINTS) {
                totalScore += SCORE_ALLCLEAR;
            }

            if (linesCleared == 4 || spinState != SPIN_NONE) {
                backToBack++;
            } else {
                backToBack = -1;
            }

            level = Math.min((int) (totalLinesCleared / 10 + 1), 20);

            totalScore += SCORE[linesCleared][spinState] * (backToBack > 0 ? 1.5 : 1) + (long) combo * SCORE_COMBO * level;
            waitForClearTicks += ((double) LINE_DELAY_MS / 1000) * TPS;
        } else {
            combo = -1;
            waitForClearTicks += ((double) ALL_DELAY_MS / 1000) * TPS;
        }

        makeNextPiece();
    }

    public void makeNextPiece() {
        while (nextPiecesLeft <= NEXTPIECESMAX) {
            int[] bag = new int[BAG_SIZE];
            for (int i = 0; i < BAG_SIZE; i++) {
                bag[i] = i;
            }
            shuffleArray(bag);
            for (int i = 0; i < BAG_SIZE; i++) {
                nextPieces[nextPiecesLeft + i] = new Piece(bag[i]);
            }
            nextPiecesLeft += BAG_SIZE;
        }

        spawnPiece();

        checkLockOut();
    }

    public boolean movePiece(int newX, int newY, int newR) {
        if (!isColliding(newX, newY, newR)) {
            counter = 0;
            current.setX(newX);
            current.setY(newY);
            current.setRotation(newR);
            spinState = SPIN_NONE;
            calcCurrentPieceLowestPossiblePosition();
            calcLimit();
            return true;
        }
        return false;
    }

    public boolean movePieceRelative(int xOffset, int yOffset) {
        return movePiece(current.getX() + xOffset, current.getY() + yOffset, current.getRotation());
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
        return new Dimension(700, 700);
    }

    public void paintComponentGAME(Graphics g) {
        long timeStart = System.nanoTime();

        g.setColor(Color.WHITE);
        g.drawString("Score: " + totalScore, 30, 30);

        drawStage(g);
        drawHold(g);
        drawNext(g);
        drawCurrent(g);

        long timeEnd = System.nanoTime();
        long timeElapsed = timeEnd - timeStart;
        g.setColor(Color.YELLOW);
        g.drawString(1000000000 / timeElapsed + " FPS", 10, 10);
        if (dead) {
            menuOpen = GAMEOVER;
        }
    }

    public void paintComponentGAMEOVER(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Game over, score: " + totalScore, 300, 300);
        g.drawString("Press R to retry", 300, 315);
        g.drawString("Press B to back", 300, 330);
    }

    public void paintComponentMAINMENU(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("Press P to play", 300, 300);
        g.drawString("Press O for options", 300, 315);
    }

    public void paintComponentOPTIONS(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString(kontrols[keysPressed], 300, 300);
    }

    public void processKeys() {
        for (int i = 0; i < a.instance.controls.length; i++) {
            if (a.instance.keyIsDown[i]) {
                if (!(onlyOnePress[i] && a.instance.keyAlreadyProcessed[i])) {
                    doAction(i);
                    a.instance.keyAlreadyProcessed[i] = true;
                }
                a.instance.howLongIsPressed[i]++;
            }
        }
    }

    public void roomLoop() {
        new Thread(() -> {
            final double expectedTickTime = 1e9 / TPS;
            long timeLast = System.nanoTime();
            long timeNow;
            double delta = 0;
            while (!dead) {
                timeNow = System.nanoTime();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
                if (!a.instance.paused) {
                    delta += (timeNow - timeLast) / expectedTickTime;
                }
                timeLast = timeNow;
                while (delta >= 1) {
                    if (waitForClearTicks < 1) {
                        tick();
                    } else {
                        waitForClearTicks--;
                    }
                    delta--;
                }
            }
        }).start();
    }

    public void rotatePiece(int d) {
        int oldRotation = current.getRotation();
        int newRotation = (current.getRotation() + d + 4) % 4;
        int piece = current.getColor();
        int state = STATES[oldRotation][newRotation];

        for (int tries = 0; tries < KICKTABLE[piece == 4 ? 1 : 0][state].length; tries++) {
            if (movePiece(current.getX() + KICKTABLE[piece == 4 ? 1 : 0][state][tries].x, current.getY() - KICKTABLE[piece == 4 ? 1 : 0][state][tries].y, newRotation)) {
                checkSpin(tries, oldRotation);
                return;
            }
        }
    }

    public void shuffleArray(int[] ar) {
        for (int i = ar.length - 1; i > 0; i--) {
            int index = ((pieceRandomizer.nextInt() % (i + 1) + i + 1)) % (i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public void spawnPiece() {
        current = nextPieces[0];
        System.arraycopy(nextPieces, 1, nextPieces, 0, nextPieces.length - 1);
        nextPiecesLeft--;
        held = false;
        spinState = SPIN_NONE;
        calcCurrentPieceLowestPossiblePosition();
        counter = 0;
        calcLimit();
    }

    public void tick() {
        processKeys();
        counter += 1 / (double) TPS;

        if (counter >= limit) {
            if (!movePieceRelative(0, +1)) {
                lockPiece();
            }
        }
        ticksPassed++;
    }
}
