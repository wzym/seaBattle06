package Model;

/**
 * Из ячеек будет состоять игровое поле и корабли
 */
public class OneCell {
    private Status status;
    private int x;
    private int y;

    public enum Status {
        DECK, WATER, BUFFER, DAMAGED_DECK, DAMAGED_SHIP, DAMAGED_WATER
    }

    public OneCell(int x, int y, Status status) {       // конструктор
        this.x = x;
        this.y = y;
        this.setStatus(status);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}