package Model.ArtificialIntelligence;

public class VariantToShot {
    private int x;
    private int y;
    private presumptiveStatus currentStatus;

    public VariantToShot(int x, int y, presumptiveStatus currentStatus) {
        this.x = x;
        this.y = y;
        this.currentStatus = currentStatus;
    }

    public enum presumptiveStatus {
        SHIP, CELL_TO_SHOT_FIRSTLY, CELL_TO_SHOT_SECONDLY, CELL_NOT_TO_SHOT
    }
}
