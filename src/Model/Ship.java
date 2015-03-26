package Model;

import java.util.HashSet;
import java.util.Set;

public class Ship {
    private String name;
    private isAlive status;
    private Set<Cell> body = new HashSet<Cell>();     // все данные о координатах теперь - это поля Field,
                                                      // поэтому корабль - это коллекция палуб

    public enum isAlive {       // статус касается состояния палубы и состояния корабля
        ALIVE, INJURED, DEAD
    }

    public Ship(String name) {
        this.name = name;
        this.status = isAlive.ALIVE;        // статус корабля - жив
    }

    public Cell getCellByCoordinates(int x, int y) {
        for (Cell cell : body) {
            if (cell.getX() == x && cell.getY() ==  y) return cell;
        }
        return null;
    }

    public Set<Cell> getBody() {
        return body;
    }

    public String getName() {
        return name;
    }
}