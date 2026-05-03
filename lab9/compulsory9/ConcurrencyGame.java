package lab9.compulsory9;

import java.util.ArrayList;
import java.util.List;


public class ConcurrencyGame {

    private static final int  ROWS           = 10;  // dimensiune labirint
    private static final int  COLS           = 10;
    private static final int  NUM_ROBOTS     = 3;   // numarul de roboti
    private static final int  BUNNY_DELAY_MS = 300; // viteza iepuras
    private static final int  ROBOT_DELAY_MS = 400; // viteza roboti
    private static final int  PRINT_EVERY_MS = 800; // cat de des afisam starea
    private static final int  MAX_STEPS      = 200; // limita antiblocare

    public static void run() throws InterruptedException
    {



        Maze maze = buildSampleMaze(ROWS, COLS);

        //starea jocului
        GameState gameState = new GameState(maze, NUM_ROBOTS);

        //Memoria partajata a robotilor
        SharedMemory sharedMemory = new SharedMemory();

        BunnyThread bunnyThread = new BunnyThread(gameState, BUNNY_DELAY_MS);

        List<RobotThread> robotThreads = new ArrayList<>();
        for (int i = 0; i < NUM_ROBOTS; i++) {
            robotThreads.add(new RobotThread(i, gameState, sharedMemory, ROBOT_DELAY_MS));
        }

        bunnyThread.start();
        for (RobotThread rt : robotThreads) rt.start();

        int steps = 0;
        while (gameState.isGameRunning() && steps < MAX_STEPS) {
            Thread.sleep(PRINT_EVERY_MS);
            gameState.printState("Pas ~" + steps);
            steps++;
        }

        bunnyThread.interrupt();
        for (RobotThread rt : robotThreads) rt.interrupt();

        bunnyThread.join(2000);
        for (RobotThread rt : robotThreads) rt.join(2000);


    }

    private static Maze buildSampleMaze(int n, int m) {
        Maze maze = new Maze(n, m);

        // Pereti pe margini
        for (int i = 0; i < n; i++) {
            maze.getCelula(i, 0).setZid(true);
            maze.getCelula(i, m-1).setZid(true);
        }
        for (int j = 0; j < m; j++) {
            maze.getCelula(0, j).setZid(true);
            maze.getCelula(n-1, j).setZid(true);
        }

        //cateva ziduri interioare
        int[][] walls = {
            {2,1},{2,2},{2,3},{2,4},{2,5},
            {4,4},{4,5},{4,6},{4,7},
            {6,2},{6,3},{6,4},
            {3,7},{4,7},{5,7},
            {7,5},{7,6},{7,7},
            {1,7},{2,7}
        };
        for (int[] w : walls)
            if (w[0] > 0 && w[0] < n-1 && w[1] > 0 && w[1] < m-1)
                maze.getCelula(w[0], w[1]).setZid(true);

        maze.getCelula(n-1, m-1).setZid(false);

        return maze;
    }
}
