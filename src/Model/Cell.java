package Model;

/**
 * Из ячеек будет состоять игровое поле. Это карта-посредник между положением дел игрока, состоянием его флота
 * и вьюшкой, а также отображением повреждённых кораблей и полей для противника.
 */
public class Cell {
    private Status status;

    public enum Status {
        DECK, WATER, BUFFER, DAMAGED_DECK, DAMAGED_SHIP, DAMAGED_WATER
    }

    public Cell(Status status) {
        this.setStatus(status);
    }       // конструктор

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
