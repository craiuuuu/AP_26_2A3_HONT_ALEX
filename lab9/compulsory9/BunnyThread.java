package lab9.compulsory9;

public class BunnyThread extends Thread {

    private final GameState gameState;
    private final int moveDelayMs; // cat de repede se misca iepurasul

    public BunnyThread(GameState gameState, int moveDelayMs) {
        super("Iepuras-Thread");
        this.gameState   = gameState;
        this.moveDelayMs = moveDelayMs;
    }

    @Override
    public void run()
    {
        System.out.println("[" + getName() + "] Pornit");

        while (gameState.isGameRunning())
        {
            // Miscam iepurasul - metoda este synchronized in GameState
            gameState.moveBunny();

            try {
                Thread.sleep(moveDelayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("[" + getName() + "] Oprit. Rezultatul " + gameState.getResult());
    }
}
