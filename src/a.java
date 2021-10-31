import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Random;

public class a extends JPanel {
    public static final int[][] PIECEMATRIX = new int[][]{
        {0b0000010001011001, 0b1000100101010110, 0b1010011001010001, 0b0010000101010100},
        {0b1000000101011001, 0b0100011001011010, 0b0010100101010001, 0b0110010001010000},
        {0b0100100001011001, 0b0100010110001001, 0b1001010110000100, 0b1001100001010100},
        {0b0100100000010101, 0b0100100101011010, 0b0110001010010101, 0b0110000101010000},
        {0b0001010110011101, 0b1000101010011011, 0b1110101001100010, 0b0111011001010100},
        {0b0000000101011001, 0b0100010110000110, 0b1010100101010001, 0b0010011001010100},
        {0b0100000101011001, 0b0100100101010110, 0b0110100101010001, 0b0110000101010100}
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
    public static final a instance = new a();
    public static final String[] kontrols = {"left", "right", "sd", "hd", "ccw", "cw", "hold"};

    public static final int[][][] KICKTABLE = new int[][][]{
        {
            {0, 0, -1, 0, -1, +1, 0, -2, -1, -2},
            {0, 0, +1, 0, +1, -1, 0, +2, +1, +2},

            {0, 0, +1, 0, +1, -1, 0, +2, +1, +2},
            {0, 0, -1, 0, -1, +1, 0, -2, -1, -2},

            {0, 0, +1, 0, +1, +1, 0, -2, +1, -2},
            {0, 0, -1, 0, -1, -1, 0, +2, -1, +2},

            {0, 0, -1, 0, -1, -1, 0, +2, -1, +2},
            {0, 0, +1, 0, +1, +1, 0, -2, +1, -2},

            {0, 0}
        },
        {
            {0, 0, -2, 0, +1, 0, -2, -1, +1, +2},
            {0, 0, +2, 0, -1, 0, +2, +1, -1, -2},

            {0, 0, -1, 0, +2, 0, -1, +2, +2, -1},
            {0, 0, +1, 0, -2, 0, +1, -2, -2, +1},

            {0, 0, +2, 0, -1, 0, +2, +1, -1, -2},
            {0, 0, -2, 0, +1, 0, -2, -1, +1, +2},

            {0, 0, +1, 0, -2, 0, +1, -2, -2, +1},
            {0, 0, -1, 0, +2, 0, -1, +2, +2, -1},

            {0, 0}
        }
    };
    public final JFrame frame = new JFrame("Notris Defect");
    public int[] controls = new int[7];
    public boolean[] keyAlreadyProcessed = new boolean[7];
    public boolean[] keyIsDown = new boolean[7];
    public int[] howLongIsPressed = new int[7];
    public int menuOpen;
    public int keysPressed = 0;
    public boolean paused = false;
    public Random pieceRandomizer;
    public boolean dead = true;
    public int[][] stage;
    public int current;
    public int currentX;
    public int currentY;
    public int currentR;
    public int[] nextPieces;
    public int nextPiecesLeft;
    public int heldPiece;
    public boolean held;
    public double counter;
    public double limit;
    public int combo;
    public int backToBack;
    public long ticksPassed;
    public long totalScore;
    public long totalLinesCleared;
    public int level;
    public double gravity;
    public int lowestPossiblePosition;
    public int spinState;
    public int waitForClearTicks;
    public int waitForShiftTicks;
    public int manipulations;
    public int lowest;

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
        int result = currentY;
        while (!isColliding(currentX, result + 1, currentR)) {
            result++;
        }
        lowestPossiblePosition = result;
    }

    public void calcGravity() {
        gravity = Math.pow(0.8 - (level - 1) * 0.007, level - 1);
    }

    public void calcLimit() {
        limit = isTouchingGround() ? 50 : (keyIsDown[2] ? gravity / 20 : gravity) * 100;
    }

    public void checkLockOut() {
        int piece = PIECEMATRIX[current][currentR];
        for (int i = 0; i < 4; i++) {
            if (stage[shift(piece, i, 0) + currentY][shift(piece, i, 1) + currentX] != 7) {
                dead = true;
            }
        }
    }

    public void checkSpin(int tries, int oldRotation) {
        int x = currentX;
        int y = currentY;
        int rot = currentR;
        boolean[] corners = new boolean[4];
        boolean[] isSignificant = {true, true, false, false};

        if (current != 6) {
            return;
        }
        if (tries == 4 && ((oldRotation == 0 || oldRotation == 2) && (rot == 1 || rot == 3))) {
            spinState = 2;
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
            spinState = 2;
        } else if (significant == 1 && others == 2) {
            spinState = 1;
        } else {
            spinState = 0;
        }
    }

    public void checkTopOut() {
        int piece = PIECEMATRIX[current][currentR];
        for (int i = 0; i < 4; i++) {
            if (currentY + shift(piece, i, 0) >= 40 - 20) {
                return;
            }
        }
        dead = true;
    }

    public void clearLine(int line) {
        for (int i = line; i > 0; i--) {
            System.arraycopy(stage[i - 1], 0, stage[i], 0, 10);
        }

        for (int j = 0; j < 10; j++) {
            stage[0][j] = 7;
        }

        totalLinesCleared++;

    }

    public int clearLines() {
        int numClears = 0;
        boolean yes;
        for (int i = 40 - 1; i > 0; i--) {
            yes = true;
            for (int j = 0; j < 10; j++) {
                if (stage[i][j] == 7) {
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
                movePieceRelative(0, 1);
                break;
            case 3:
                int lines = 0;
                while (!isColliding(currentX, currentY + lines + 1, currentR)) {
                    lines++;
                }
                if (lines > 0) {
                    movePieceRelative(0, +lines);
                    totalScore += (long) lines * 2;
                }
                lockPiece();
                break;
            case 4:
                rotatePiece(-1);
                break;
            case 5:
                rotatePiece(1);
                break;
            case 6:
                if (!held) {
                    if (heldPiece == -1) {
                        heldPiece = current;
                        makeNextPiece();
                    } else {
                        int temp = current;
                        current = heldPiece;
                        manipulations = 0;
                        movePiece(3, 18, 0);
                        lowest = 0;
                        manipulations = 0;
                        if (movePieceRelative(0, 1) && keyIsDown[2]) {
                            totalScore--;
                        }
                        heldPiece = temp;
                        calcCurrentPieceLowestPossiblePosition();
                        counter = 0;
                        calcLimit();
                        checkLockOut();
                    }
                    held = true;
                }
                break;
        }
    }

    public void drawPix(Graphics g, int tlcx, int tlcy, int x, int y) {
        g.fillRect(tlcx + x * 33, tlcy + y * 33, 32, 32);
    }

    public void init() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("controls.txt"));
            for (int i = 0; i < 7; i++) {
                controls[i] = Integer.parseInt(br.readLine());
            }
        } catch (Exception ignored) {
        }
        menuOpen = 0;
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(instance);
        frame.pack();
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setLocation(300, 50);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (menuOpen) {
                    case 0:
                        if (e.getKeyCode() == KeyEvent.VK_P) {
                            init2();
                            menuOpen = 2;
                        } else if (e.getKeyCode() == KeyEvent.VK_O) {
                            menuOpen = 1;
                        }
                        break;
                    case 1:
                        controls[keysPressed] = e.getKeyCode();
                        keysPressed++;
                        if (keysPressed == 7) {
                            keysPressed = 0;
                            try {
                                FileWriter fw = new FileWriter("controls.txt");
                                for (int control : controls) {
                                    fw.write(control + "\n");
                                }
                                fw.close();
                            } catch (Exception ex) {
                                new File("controls.txt");
                            }
                            menuOpen = 0;
                        } else {
                            repaint();
                        }
                        break;
                    case 2:
                        if (e.getKeyCode() == KeyEvent.VK_E) {
                            paused ^= true;
                        } else if (e.getKeyCode() == KeyEvent.VK_B) {
                            dead = true;
                            menuOpen = 0;
                        }

                        int key = e.getKeyCode();
                        for (int i = 0; i < 7; i++) {
                            if (key == controls[i]) {
                                keyIsDown[i] = true;
                                break;
                            }
                        }
                        break;
                    case 3:
                        if (e.getKeyCode() == KeyEvent.VK_R) {
                            init2();
                            menuOpen = 2;
                        } else if (e.getKeyCode() == KeyEvent.VK_B) {
                            menuOpen = 0;
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (menuOpen == 2) {
                    int key = e.getKeyCode();
                    for (int i = 0; i < 7; i++) {
                        if (key == controls[i]) {
                            keyIsDown[i] = false;
                            keyAlreadyProcessed[i] = false;
                            howLongIsPressed[i] = 0;
                            break;
                        }
                    }
                }
            }
        });
    }

    public void init2() {
        pieceRandomizer = new Random();
        dead = false;
        paused = false;
        stage = new int[40][10];
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 10; j++) {
                stage[i][j] = 7;
            }
        }

        lowest = 0;
        manipulations = 0;
        waitForClearTicks = 0;
        waitForShiftTicks = 0;

        nextPieces = new int[5 + 7];
        nextPiecesLeft = 0;

        heldPiece = -1;
        held = false;

        counter = 0;

        combo = -1;
        backToBack = -1;

        ticksPassed = 0;
        totalScore = 0;
        totalLinesCleared = 0;

        level = 1;

        calcGravity();
        makeNextPiece();
        roomLoop();
    }

    public boolean isColliding(int x, int y, int rotation) {
        int temp = PIECEMATRIX[current][rotation];
        for (int i = 0; i < 4; i++) {
            int tx = shift(temp, i, 1);
            int ty = shift(temp, i, 0);
            if (isInsideBounds(x + tx, y + ty)) {
                if (stage[y + ty][x + tx] != 7) {
                    return true;
                }
            } else {
                return true;
            }
        }
        return false;
    }

    public boolean isInsideBounds(int x, int y) {
        return y >= 0 && 40 > y && x >= 0 && 10 > x;
    }

    public boolean isSolid(int x, int y) {
        return !isInsideBounds(x, y) || stage[y][x] != 7;
    }

    public boolean isTouchingGround() {
        return isColliding(currentX, currentY + 1, currentR);
    }

    public void lockPiece() {
        int temp = PIECEMATRIX[current][currentR];

        for (int i = 0; i < 4; i++) {
            stage[currentY + shift(temp, i, 0)][currentX + shift(temp, i, 1)] = current;
        }

        checkTopOut();

        int linesCleared = clearLines();

        if (linesCleared > 0) {
            combo++;

            if (linesCleared == 4 || spinState != 0) {
                backToBack++;
            } else {
                backToBack = -1;
            }

            level = Math.min((int) (totalLinesCleared / 10 + 1), 20);
            calcGravity();

            totalScore += (SCORE[linesCleared][spinState] * (backToBack > 0 ? 1.5 : 1) + (long) combo * 50) * level;
            waitForClearTicks += 50;
        } else {
            combo = -1;
            waitForClearTicks += 10;
        }

        makeNextPiece();
    }

    public void makeNextPiece() {
        while (nextPiecesLeft <= 5) {
            int[] bag = new int[7];
            for (int i = 0; i < 7; i++) {
                bag[i] = i;
            }
            for (int i = bag.length - 1; i > 0; i--) {
                int index = ((pieceRandomizer.nextInt() % (i + 1) + i + 1)) % (i + 1);
                int a = bag[index];
                bag[index] = bag[i];
                bag[i] = a;
            }
            System.arraycopy(bag, 0, nextPieces, nextPiecesLeft, 7);
            nextPiecesLeft += 7;
        }

        spawnPiece();

        checkLockOut();
    }

    public boolean movePiece(int newX, int newY, int newR) {
        if (!isColliding(newX, newY, newR)) {
            counter = 0;
            manipulations++;
            if (keyIsDown[2]) {
                totalScore += Math.max(newY - currentY, 0);
            }
            currentX = newX;
            currentY = newY;
            currentR = newR;
            spinState = 0;
            calcCurrentPieceLowestPossiblePosition();
            calcLimit();
            if (manipulations > 15) {
                manipulations = 0;
                if (!movePieceRelative(0, +1)) {
                    lockPiece();
                }
            } else if (newY > lowest) {
                lowest = newY;
                manipulations = 0;
            }
            return true;
        }
        return false;
    }

    public boolean movePieceRelative(int xOffset, int yOffset) {
        return movePiece(currentX + xOffset, currentY + yOffset, currentR);
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(0, 0, 720, 720);
        switch (menuOpen) {
            case 0:
                g.setColor(Color.BLACK);
                g.drawString("Play", 300, 300);
                g.drawString("Options", 300, 315);
                break;
            case 1:
                g.setColor(Color.BLACK);
                g.drawString(kontrols[keysPressed], 300, 300);
                break;
            case 2:
                g.setColor(Color.BLACK);
                g.drawString(paused ? "resumE" : "pausE", 30, 300);
                g.drawString("Back", 30, 315);

                g.drawString("score: " + totalScore, 30, 400);

                // stage hold next current
                g.setColor(Color.WHITE);
                g.fillRect(185, 12, 350, 703);
                for (int i = 19; i < 40; i++) {
                    for (int j = 0; j < 10; j++) {
                        g.setColor(intToColor(stage[i][j]));
                        drawPix(g, 195, -615, j, i);
                    }
                }

                g.setColor(Color.BLACK);
                g.fillRect(30, 45, 132, 132);
                if (heldPiece != -1) {
                    g.setColor(intToColor(heldPiece));
                    int piece = PIECEMATRIX[heldPiece][0];
                    for (int i = 0; i < 4; i++) {
                        drawPix(g, 30, 45, shift(piece, i, 1), shift(piece, i, 0));
                    }
                }

                g.setColor(Color.BLACK);
                g.fillRect(558, 45, 132, 660);
                for (int i = 0; i < 5; i++) {
                    int piece = nextPieces[i];
                    g.setColor(intToColor(piece));
                    int piecei = PIECEMATRIX[piece][0];
                    for (int j = 0; j < 4; j++) {
                        drawPix(g, 558, 45, shift(piecei, j, 1), i * 4 + shift(piecei, j, 0));
                    }
                }

                int piece = PIECEMATRIX[current][currentR];
                if (waitForClearTicks == 0) {
                    for (int i = 0; i < 4; i++) {
                        g.setColor(Color.WHITE);
                        drawPix(g, 195, -615, shift(piece, i, 1) + currentX, shift(piece, i, 0) + lowestPossiblePosition);
                    }
                    for (int i = 0; i < 4; i++) {
                        g.setColor(intToColor(current));
                        drawPix(g, 195, -615, shift(piece, i, 1) + currentX, shift(piece, i, 0) + currentY);
                    }
                }

                if (dead) {
                    menuOpen = 3;
                }
                break;
            case 3:
                g.setColor(Color.BLACK);
                g.drawString("score: " + totalScore, 300, 300);
                g.drawString("Retry", 300, 315);
                g.drawString("Back", 300, 330);
                break;
        }
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(720, 720);
    }

    public void processKeys() {
        for (int i = 0; i < 7; i++) {
            if (keyIsDown[i]) {
                if (!keyAlreadyProcessed[i]) {
                    doAction(i);
                    keyAlreadyProcessed[i] = true;
                }
                howLongIsPressed[i]++;
            }
        }
        if ((howLongIsPressed[0] - 16) >= 0 && (howLongIsPressed[0] - 16) % 2 == 0) {
            doAction(0);
        }
        if ((howLongIsPressed[1] - 16) >= 0 && (howLongIsPressed[1] - 16) % 2 == 0) {
            doAction(1);
        }

    }

    public void roomLoop() {
        new Thread(() -> {
            final double expectedTickTime = 1e7;
            long timeLast = System.nanoTime();
            long timeNow;
            double delta = 0;
            while (!dead) {
                timeNow = System.nanoTime();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
                if (!paused) {
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
        int oldRotation = currentR;
        int newRotation = (currentR + d + 4) % 4;
        int piece = current;
        int state = STATES[oldRotation][newRotation];

        for (int tries = 0; tries < KICKTABLE[piece == 4 ? 1 : 0][state].length / 2; tries++) {
            if (movePiece(currentX + KICKTABLE[piece == 4 ? 1 : 0][state][tries * 2], currentY - KICKTABLE[piece == 4 ? 1 : 0][state][tries * 2 + 1], newRotation)) {
                checkSpin(tries, oldRotation);
                return;
            }
        }
    }

    public int shift(int n, int i, int or) {
        return n >> 2 * (i * 2 + or) & 0b11;
    }

    public void spawnPiece() {
        current = nextPieces[0];
        manipulations = 0;
        movePiece(3, 18, 0);
        lowest = 0;
        manipulations = 0;
        if (movePieceRelative(0, 1) && keyIsDown[2]) {
            totalScore--;
        }
        System.arraycopy(nextPieces, 1, nextPieces, 0, nextPieces.length - 1);
        nextPiecesLeft--;
        held = false;
        spinState = 0;
        calcCurrentPieceLowestPossiblePosition();
        counter = 0;
        calcLimit();
    }

    public void tick() {
        processKeys();
        if (++counter >= limit) {
            if (!movePieceRelative(0, 1)) {
                lockPiece();
            }
        }
        ticksPassed++;
    }
}
