package lab9.compulsory9;

public class RobotThread extends Thread {

    private final int         robotIndex;
    private final GameState   gameState;
    private final SharedMemory sharedMemory;
    private final int         moveDelayMs;

    public RobotThread(int robotIndex, GameState gameState,
                       SharedMemory sharedMemory, int moveDelayMs) {
        super("robot" + robotIndex + "Thread");
        this.robotIndex   = robotIndex;
        this.gameState    = gameState;
        this.sharedMemory = sharedMemory;
        this.moveDelayMs  = moveDelayMs;
    }

    @Override
    public void run() {
        System.out.println("[" + getName() + "] Pornit");

        while (gameState.isGameRunning()) {

            int[] target = sharedMemory.getBunnyPosition();

            gameState.moveRobot(robotIndex, target);

            int[] nearBunny = gameState.checkNearBunny(robotIndex);
            if (nearBunny != null)
            {
                sharedMemory.reportBunnyPosition(nearBunny[0], nearBunny[1], getName());
            }

            try {
                Thread.sleep(moveDelayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("[" + getName() + "] Oprit.Rezultat " + gameState.getResult());
    }
}
