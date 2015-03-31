package Model.ArtificialIntelligence;

public class VariantToShot {
    private int x;
    private int y;
    private PresumptiveStatus currentStatus;

    public VariantToShot(int x, int y, PresumptiveStatus currentStatus) {
        this.x = x;
        this.y = y;
        this.currentStatus = currentStatus;
    }

    public enum PresumptiveStatus {
        CELL_TO_SHOT, CELL_NOT_TO_SHOT
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PresumptiveStatus getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(PresumptiveStatus currentStatus) {
        this.currentStatus = currentStatus;
    }
}
