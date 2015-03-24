package Model;

import java.util.HashSet;
import java.util.Set;

public class Ship {
    private String name;
    private boolean isHorizontal;
    private isAlive status;
    private Set<Cell> body = new HashSet<Cell>();     // все данные о координатах теперь - это поля Field,
                                                      // поэтому корабль - это коллекция палуб

    public enum isAlive {       // статус касается состояния палубы и состояния корабля
        ALIVE, INJURED, DEAD
    }

    public Ship(boolean isHorizontal) {
        this.isHorizontal = isHorizontal;
        this.status = isAlive.ALIVE;        // статус корабля - жив
    }

    public Set<Cell> getBody() {
        return body;
    }
}