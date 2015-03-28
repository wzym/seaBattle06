package Model;

/**
 * Корабль состоит из тех же ячеек, что и поле игры. Каждая
 */
import java.util.HashSet;
import java.util.Set;

public class Ship {
    private String name;
    private isAlive status;
    private Set<OneCell> body = new HashSet<OneCell>();     // корабль - это коллекция палуб-ячеек
    private int health;

    public enum isAlive {       // статус касается состояния корабля
        ALIVE, INJURED, DEAD
    }

    public Ship(String name, int length) {
        this.name = name;
        this.health = length;
        this.status = isAlive.ALIVE;        // статус корабля - жив
    }

    /**
     * Возвращает по координатам ячейку, входящую в состав корабля. Если таковой нет - null.
     * @param x
     * @param y
     * @return
     */
    public OneCell getCellByCoordinates(int x, int y) {
        for (OneCell deck : body) {
            if (deck.getX() == x && deck.getY() ==  y) return deck;
        }
        return null;
    }

    /**
     * При получении повреждения уменьшается здоровье, при достижении ноля статус меняется на DEAD.
     * Соответственно выставляется статус ячеек-палуб корабля.
     * @param x
     * @param y
     * @return
     */
    public isAlive getDamage(int x, int y) {
        OneCell deck = getCellByCoordinates(x, y);
        this.health--;
        this.health = (this.health < 0) ? (0) : this.health;
        if (this.health > 0) {
            this.status = isAlive.INJURED;
            deck.setStatus(OneCell.Status.DAMAGED_DECK);
        } else {
            this.status = isAlive.DEAD;
            for (OneCell deadDeck : body) {
                deadDeck.setStatus(OneCell.Status.DAMAGED_SHIP);
            }
        }
        return this.status;
    }

    public Set<OneCell> getBody() {
        return body;
    }

    public String getName() {
        return name;
    }

    public isAlive getStatus() {
        return status;
    }
}