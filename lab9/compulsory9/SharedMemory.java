package lab9.compulsory9;

/**
 * Memorie partajata intre roboti (thread-safe).
 * Robotii pot stoca/citi ultima pozitie cunoscuta a iepurasului.
 */
public class SharedMemory {

    private int bunnyRow = -1;
    private int bunnyCol = -1;
    private boolean bunnyPositionKnown = false;

    /**
     * Un robot raporteaza pozitia iepurasului (daca il vede in vecinatate).
     */
    public synchronized void reportBunnyPosition(int row, int col, String robotName) {
        this.bunnyRow = row;
        this.bunnyCol = col;
        this.bunnyPositionKnown = true;
        System.out.println("  [SharedMemory] " + robotName
                + " a actualizat pozitia iepurasului: (" + row + "," + col + ")");
    }

    /**
     * Returneaza ultima pozitie cunoscuta a iepurasului,
     * sau null daca nu se stie nimic.
     */
    public synchronized int[] getBunnyPosition() {
        if (!bunnyPositionKnown) return null;
        return new int[]{bunnyRow, bunnyCol};
    }

    public synchronized void reset() {
        bunnyPositionKnown = false;
        bunnyRow = -1;
        bunnyCol = -1;
    }
}
