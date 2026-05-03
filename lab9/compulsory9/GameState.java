package lab9.compulsory9;

import lombok.Getter;

import java.util.Random;

public class GameState {

    public enum GameResult { ONGOING, BUNNY_ESCAPED, BUNNY_CAUGHT }

    private final Maze maze;
    private final int exitRow;
    private final int exitCol;

    private int   bunnyRow, bunnyCol;
    private final int[] robotRows;
    private final int[] robotCols;

    @Getter
    private volatile GameResult result      = GameResult.ONGOING;
    @Getter
    private volatile boolean    gameRunning = true;

    private final Random rng = new Random();

    public GameState(Maze maze, int numRobots) {
        this.maze    = maze;
        this.exitRow = maze.getN() - 1;
        this.exitCol = maze.getM() - 1;

        int[][] free = freeCells();
        shuffle(free);

        bunnyRow = free[0][0];  bunnyCol = free[0][1];
        robotRows = new int[numRobots];
        robotCols = new int[numRobots];
        for (int i = 0; i < numRobots; i++) {
            robotRows[i] = free[i + 1][0];
            robotCols[i] = free[i + 1][1];
        }

        maze.getCelula(exitRow, exitCol).setZid(false);

        System.out.println("[Iepuras -(" + bunnyRow + "," + bunnyCol + ")");
        for (int i = 0; i < numRobots; i++)
            System.out.println("Robot" + i + "  (" + robotRows[i] + "," + robotCols[i] + ")");
        System.out.println("Iesire  (" + exitRow + "," + exitCol + ")");
    }

    public synchronized boolean moveBunny()
    {
        if (!gameRunning) return false;

        for (int[] d : shuffledDirs())
        {
            int nr = bunnyRow + d[0], nc = bunnyCol + d[1];
            if (isFree(nr, nc) && !robotAt(nr, nc)) {
                bunnyRow = nr;  bunnyCol = nc;
                if (nr == exitRow && nc == exitCol) {
                    result      = GameResult.BUNNY_ESCAPED;
                    gameRunning = false;
                    System.out.println(" Iepurasul a reusit sa scape (" + nr + "," + nc + ")");
                }
                return true;
            }
        }
        return false; // complet blocat
    }

    public synchronized boolean moveRobot(int idx, int[] targetPos) {
        if (!gameRunning) return false;

        int[][] dirs = (targetPos != null)
                ? directedDirs(robotRows[idx], robotCols[idx], targetPos[0], targetPos[1])
                : shuffledDirs();

        for (int[] d : dirs) {
            int nr = robotRows[idx] + d[0], nc = robotCols[idx] + d[1];
            if (isFree(nr, nc) && !otherRobotAt(idx, nr, nc)) {
                robotRows[idx] = nr;  robotCols[idx] = nc;
                if (nr == bunnyRow && nc == bunnyCol) {
                    result      = GameResult.BUNNY_CAUGHT;
                    gameRunning = false;
                    System.out.println("Robotu" + idx + " a prins iepurasul la ("
                            + nr + "," + nc + ")");
                }
                return true;
            }
        }
        return false;
    }

    public synchronized int[] checkNearBunny(int idx) {
        int rr = robotRows[idx], rc = robotCols[idx];
        if (Math.abs(rr - bunnyRow) + Math.abs(rc - bunnyCol) <= 2)
            return new int[]{bunnyRow, bunnyCol};
        return null;
    }

    public synchronized void printState(String label) {
        System.out.println("B=Iepuras  R=Robot  E=Iesire  #=Zid  .=Liber");
        int n = maze.getN(), m = maze.getM();
        for (int i = 0; i < n; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < m; j++) {
                if      (maze.getCelula(i, j).esteZid())       sb.append('#');
                else if (i == bunnyRow  && j == bunnyCol)      sb.append('B');
                else if (i == exitRow   && j == exitCol)       sb.append('E');
                else if (robotAt(i, j))                        sb.append('R');
                else                                           sb.append('.');
            }
            System.out.println(sb);
        }
        System.out.println("Iepuras(" + bunnyRow + "," + bunnyCol + ")");
        for (int i = 0; i < robotRows.length; i++)
            System.out.println("Robot"  + i + " (" + robotRows[i] + "," + robotCols[i] + ")");
    }

    private boolean isFree(int r, int c)
    {
        return r >= 0 && r < maze.getN() && c >= 0 && c < maze.getM()
                && !maze.getCelula(r, c).esteZid();
    }

    private boolean robotAt(int r, int c) {
        for (int i = 0; i < robotRows.length; i++)
            if (robotRows[i] == r && robotCols[i] == c) return true;
        return false;
    }

    private boolean otherRobotAt(int myIdx, int r, int c) {
        for (int i = 0; i < robotRows.length; i++)
            if (i != myIdx && robotRows[i] == r && robotCols[i] == c) return true;
        return false;
    }

    private int[][] freeCells() {
        int n = maze.getN(), m = maze.getM(), k = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (!maze.getCelula(i, j).esteZid()) k++;
        int[][] cells = new int[k][2]; k = 0;
        for (int i = 0; i < n; i++)
            for (int j = 0; j < m; j++)
                if (!maze.getCelula(i, j).esteZid()) { cells[k][0]=i; cells[k][1]=j; k++; }
        return cells;
    }

    private void shuffle(int[][] arr) {
        for (int i = arr.length - 1; i > 0; i--)
        {
            int j = rng.nextInt(i + 1);
            int[] t = arr[i]; arr[i] = arr[j]; arr[j] = t;
        }
    }

    private int[][] shuffledDirs()
    {
        int[][] d = {{-1,0},{1,0},{0,-1},{0,1}};
        for (int i = 3; i > 0; i--)
        {
            int j = rng.nextInt(i + 1);
            int[] t = d[i]; d[i] = d[j]; d[j] = t;
        }
        return d;
    }

    private int[][] directedDirs(int fr, int fc, int tr, int tc) {
        int[][] all = shuffledDirs();
        int[][] res = new int[4][2];
        int idx = 0;
        int oldDist = Math.abs(fr - tr) + Math.abs(fc - tc);
        for (int[] d : all)
            if (Math.abs(fr+d[0]-tr) + Math.abs(fc+d[1]-tc) < oldDist) res[idx++] = d;
        for (int[] d : all)
            if (Math.abs(fr+d[0]-tr) + Math.abs(fc+d[1]-tc) >= oldDist) res[idx++] = d;
        return res;
    }
}
