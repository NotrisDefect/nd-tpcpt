package challenge;

import java.awt.Point;
import java.util.Random;


public class Table {
    public static final byte PIECE_T = 6;
    public static final byte PIECE_NONE = 7;
    public static final byte SPIN_NONE = 0;
    public static final byte SPIN_MINI = 1;
    public static final byte SPIN_FULL = 2;
    public static final int TPS = 100;
    public static final int ALL_DELAY_MS = 100;
    public static final int LINE_DELAY_MS = 500;
    public static final int ARR_MS = 20;
    public static final int DAS_MS = 160;
    public static final int MANIPS = 15;
    private static final Point[][][] PIECES = new Point[][][]{
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
    private static final byte[][] STATES = {
        {-1, 0, 8, 7},
        {1, -1, 2, 10},
        {9, 3, -1, 4},
        {6, 11, 5, -1}
    };
    private static final int[][] SCORE = {
        {0, 100, 400},
        {100, 200, 800},
        {300, 400, 1200},
        {500, 0, 1600},
        {800, 0, 2600}
    };
    private static final int SCORE_ALLCLEAR = 3500;
    private static final int SCORE_COMBO = 50;
    private static final int SCORE_SOFTDROP = 1;
    private static final int SCORE_HARDDROP = 2;
    private static final int BAG_SIZE = 7;
    private static final int PIECEPOINTS = 4;
    private static final double gravityMulti = 20;
    public final KickTable KICKTABLE = new KickTable(new Point[][][]{
        {//J, L, S, T, Z Tetromino Wall Kick Data

            {new Point(0, 0), new Point(-1, 0), new Point(-1, +1), new Point(0, -2), new Point(-1, -2)},
            {new Point(0, 0), new Point(+1, 0), new Point(+1, -1), new Point(0, +2), new Point(+1, +2)},

            {new Point(0, 0), new Point(+1, 0), new Point(+1, -1), new Point(0, +2), new Point(+1, +2)},
            {new Point(0, 0), new Point(-1, 0), new Point(-1, +1), new Point(0, -2), new Point(-1, -2)},

            {new Point(0, 0), new Point(+1, 0), new Point(+1, +1), new Point(0, -2), new Point(+1, -2)},
            {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(0, +2), new Point(-1, +2)},

            {new Point(0, 0), new Point(-1, 0), new Point(-1, -1), new Point(0, +2), new Point(-1, +2)},
            {new Point(0, 0), new Point(+1, 0), new Point(+1, +1), new Point(0, -2), new Point(+1, -2)},

            {},
            {},

            {},
            {},
        },
        {//I Tetromino Wall Kick Data

            {new Point(0, 0), new Point(-2, 0), new Point(+1, 0), new Point(-2, -1), new Point(+1, +2)},
            {new Point(0, 0), new Point(+2, 0), new Point(-1, 0), new Point(+2, +1), new Point(-1, -2)},

            {new Point(0, 0), new Point(-1, 0), new Point(+2, 0), new Point(-1, +2), new Point(+2, -1)},
            {new Point(0, 0), new Point(+1, 0), new Point(-2, 0), new Point(+1, -2), new Point(-2, +1)},

            {new Point(0, 0), new Point(+2, 0), new Point(-1, 0), new Point(+2, +1), new Point(-1, -2)},
            {new Point(0, 0), new Point(-2, 0), new Point(+1, 0), new Point(-2, -1), new Point(+1, +2)},

            {new Point(0, 0), new Point(+1, 0), new Point(-2, 0), new Point(+1, -2), new Point(-2, +1)},
            {new Point(0, 0), new Point(-1, 0), new Point(+2, 0), new Point(-1, +2), new Point(+2, -1)},

            {},
            {},

            {},
            {},
        }
    });
    private final double lockDelay = 0.5d;
    protected int STAGESIZEX = 10;
    protected int STAGESIZEY = 40;
    public final int[] MANIPS_USED = new int[STAGESIZEY];
    protected int PLAYABLEROWS = 20;
    protected int NEXTPIECESMAX = 5;
    private Random pieceRandomizer;
    private boolean dead = true;
    private int[][] stage;
    private Piece current;
    private Piece[] nextPieces;
    private int nextPiecesLeft;
    private Piece heldPiece;
    private boolean held;
    private double counter;
    private double limit;
    private int combo;
    private int backToBack;
    private long ticksPassed;
    private long totalScore;
    private long totalLinesCleared;
    private long totalPiecesPlaced;
    private int level;
    private double gravity;
    private int lowestPossiblePosition;
    private int spinState;
    private int waitForClearTicks = 0;

    public void extAbortGame() {
        die();
    }

    public void extDropPieceHard() {
        hardDropPiece();
    }

    public void extDropPieceSoft() {
        movePieceRelative(0, 1);
        totalScore += Math.max(0, SCORE_SOFTDROP);
    }

    public void extDropPieceSoftMax() {
        instantSoftDrop();
    }

    public void extHoldPiece() {
        holdPiece();
    }

    public void extMovePieceLeft() {
        movePieceRelative(-1, 0);
    }

    public void extMovePieceLeftMax() {
        dasLeft();
    }

    public void extMovePieceRight() {
        movePieceRelative(1, 0);
    }

    public void extMovePieceRightMax() {
        dasRight();
    }

    public void extRotatePieceCCW() {
        rotatePiece(-1);
    }

    public void extRotatePieceCW() {
        rotatePiece(1);
    }

    public void extRotatePiece180() {
        rotatePiece(2);
    }

    public void extStartGame() {
        extStartGame(new Random().nextLong());
    }

    public void extStartGame(double seed) {
        pieceRandomizer = new Random((long) seed);
        initGame();
    }

    public void extTick() {
        tick();
    }

    public int getBackToBack() {
        return backToBack;
    }

    public int getCombo() {
        return combo;
    }

    public double getCounter() {
        return counter;
    }

    public Piece getCurrentPiece() {
        return current;
    }

    public Piece getHeldPiece() {
        return heldPiece;
    }

    public int getLowestPossiblePosition() {
        return lowestPossiblePosition;
    }

    public Piece[] getNextPieces() {
        return nextPieces;
    }

    public Point[] getPoints(int piece, int rotation) {
        return PIECES[piece][rotation];
    }

    public int[][] getStage() {
        return stage;
    }

    public long getTicksPassed() {
        return ticksPassed;
    }

    public long getTotalLinesCleared() {
        return totalLinesCleared;
    }

    public long getTotalPiecesPlaced() {
        return totalPiecesPlaced;
    }

    public long getTotalScore() {
        return totalScore;
    }

    public boolean isDead() {
        return dead;
    }

    public void processKeys() {
        for (int i = 0; i < Game.keys.length; i++) {
            if (Game.keyIsDown[i]) {
                if (!(Game.onlyOnePress[i] && Game.keyAlreadyProcessed[i])) {
                    doAction(i);
                    Game.keyAlreadyProcessed[i] = true;
                }
                Game.howLongIsPressed[i]++;
            }
        }
    }

    protected void evtGameover() {

    }

    protected void evtLineClear(int row, int[] content) {

    }

    protected void evtLockPiece(Piece piece, int linesCleared, int spinState, int combo, int backToBack) {

    }

    protected void evtPerfectClear() {

    }

    protected void evtSpin() {

    }

    private void calcCurrentPieceLowestPossiblePosition() {
        int result = current.getY();
        while (!isColliding(current.getX(), result + 1, current.getRotation())) {
            result++;
        }
        lowestPossiblePosition = result;
    }

    private void calcGravity() {
        gravity = Math.pow(0.8 - (level - 1) * 0.007, level - 1);
    }

    private void calcLimit() {
        limit = isTouchingGround() ? lockDelay : (Game.keyIsDown[2] ? gravity / gravityMulti : gravity);
    }

    private void checkLockOut() {
        Point[] piece = PIECES[current.getColor()][current.getRotation()];
        for (int i = 0; i < PIECEPOINTS; i++) {
            Point point = piece[i];
            if (stage[point.y + current.getY()][point.x + current.getX()] != PIECE_NONE) {
                die();
            }
        }
    }

    private void checkSpin(int tries, int oldRotation) {
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
            return;
        }

        evtSpin();
    }

    private void checkTopOut() {
        Point[] piece = PIECES[current.getColor()][current.getRotation()];
        for (int i = 0; i < PIECEPOINTS; i++) {
            Point point = piece[i];
            if (current.getY() + point.y >= STAGESIZEY - PLAYABLEROWS) {
                return;
            }
        }
        die();
    }

    private void clearLine(int line) {

        evtLineClear(line, stage[line].clone());

        for (int i = line; i > 0; i--) {
            System.arraycopy(stage[i - 1], 0, stage[i], 0, STAGESIZEX);
        }

        for (int j = 0; j < STAGESIZEX; j++) {
            stage[0][j] = PIECE_NONE;
        }

        totalLinesCleared++;

    }

    private int clearLines() {
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

    private void dasLeft() {
        int num = 0;
        int x = current.getX();
        while (!isColliding(x + num - 1, current.getY(), current.getRotation())) {
            num--;
        }
        movePieceRelative(num, 0);
    }

    private void dasRight() {
        int num = 0;
        int x = current.getX();
        while (!isColliding(x + num + 1, current.getY(), current.getRotation())) {
            num++;
        }
        movePieceRelative(num, 0);
    }

    private void die() {
        dead = true;
        evtGameover();
    }

    private void doAction(int i) {
        switch (i) {
            case 0:
                extMovePieceLeft();
                break;
            case 1:
                extMovePieceRight();
                break;
            case 2:
                extDropPieceSoft();
                break;
            case 3:
                extDropPieceHard();
                break;
            case 4:
                extRotatePieceCCW();
                break;
            case 5:
                extRotatePieceCW();
                break;
            case 6:
                extRotatePiece180();
                break;
            case 7:
                extHoldPiece();
                break;
        }
    }

    private void hardDropPiece() {
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

    private void holdPiece() {
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

    private void initGame() {
        dead = false;
        Game.paused = false;
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

    private void instantSoftDrop() {
        int num = 0;
        int y = current.getY();
        while (!isColliding(current.getX(), y + num + 1, current.getRotation())) {
            num++;
        }
        totalScore += (long) SCORE_SOFTDROP * num;
        movePieceRelative(0, num);
    }

    private boolean isColliding(int x, int y, int rotation) {
        Point[] temp;

        temp = PIECES[current.getColor()][rotation];
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

    private boolean isInsideBounds(int x, int y) {
        return y >= 0 && STAGESIZEY > y && x >= 0 && STAGESIZEX > x;
    }

    private boolean isSolid(int x, int y) {
        return !isInsideBounds(x, y) || stage[y][x] != PIECE_NONE;
    }

    private boolean isTouchingGround() {
        return isColliding(current.getX(), current.getY() + 1, current.getRotation());
    }

    private void lockPiece() {
        Point[] temp;

        totalPiecesPlaced++;

        temp = PIECES[current.getColor()][current.getRotation()];
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
                evtPerfectClear();
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

        evtLockPiece(current, linesCleared, spinState, combo, backToBack);

        makeNextPiece();
    }

    private void makeNextPiece() {
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

    private boolean movePiece(int newX, int newY, int newR) {
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

    private boolean movePieceRelative(int xOffset, int yOffset) {
        return movePiece(current.getX() + xOffset, current.getY() + yOffset, current.getRotation());
    }

    private void roomLoop() {
        new Thread(() -> {
            final double expectedTickTime = 1e9 / Table.TPS;
            long timeLast = System.nanoTime();
            long timeNow;
            double delta = 0;
            while (!dead) {
                timeNow = System.nanoTime();
                try {
                    Thread.sleep(1);
                } catch (InterruptedException ignored) {
                }
                if (!Game.paused) {
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

    private void rotatePiece(int d) {
        int oldRotation = current.getRotation();
        int newRotation = (current.getRotation() + d + 4) % 4;
        int piece = current.getColor();
        int state = STATES[oldRotation][newRotation];

        for (int tries = 0; tries < KICKTABLE.maxTries(piece, state); tries++) {
            if (movePiece(current.getX() + KICKTABLE.getX(piece, state, tries), current.getY() - KICKTABLE.getY(piece, state, tries), newRotation)) {
                checkSpin(tries, oldRotation);
                return;
            }
        }

    }

    private void shuffleArray(int[] ar) {
        for (int i = ar.length - 1; i > 0; i--) {
            int index = ((pieceRandomizer.nextInt() % (i + 1) + i + 1)) % (i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    private void spawnPiece() {
        current = nextPieces[0];
        System.arraycopy(nextPieces, 1, nextPieces, 0, nextPieces.length - 1);
        nextPiecesLeft--;
        held = false;
        spinState = SPIN_NONE;
        calcCurrentPieceLowestPossiblePosition();
        counter = 0;
        calcLimit();
    }

    private void tick() {
        processKeys();
        counter += 1 / (double) TPS;

        if (counter >= limit) {
            if (!movePieceRelative(0, +1)) {
                lockPiece();
            }
        }
        ticksPassed++;
    }

    public class KickTable {

        private final Point[][][] kicks;

        public KickTable(Point[][][] kicks) {
            this.kicks = kicks;
        }

        public int getX(int piece, int state, int tries) {
            return kicks[piece == 4 ? 1 : 0][state][tries].x;
        }

        public int getY(int piece, int state, int tries) {
            return kicks[piece == 4 ? 1 : 0][state][tries].y;
        }

        public int maxTries(int piece, int state) {
            return kicks[piece == 4 ? 1 : 0][state].length;
        }

    }

    public class Piece {

        private int color;
        private int x;
        private int y;
        private int rotation;

        public Piece(Piece p) {
            this(p.getColor());
        }

        public Piece(int color) {
            this(color, 3, 21, 0);
        }

        public Piece(int color, int x, int y, int rotation) {
            this.color = color;
            this.x = x;
            this.y = y;
            this.rotation = rotation;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public int getRotation() {
            return rotation;
        }

        public void setRotation(int rotation) {
            this.rotation = rotation;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

    }
}
