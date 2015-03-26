package Model;

import java.util.HashSet;
import java.util.Set;

public class Ship {
    private String name;
    private isAlive status;
    private Set<Cell> body = new HashSet<Cell>();     // корабль - это коллекция палуб-ячеек
    private int health;

    public enum isAlive {       // статус касается состояния палубы и состояния корабля и палубы
        ALIVE, INJURED, DEAD
    }

    public Ship(String name, int length) {
        this.name = name;
        this.health = length;
        this.status = isAlive.ALIVE;        // статус корабля - жив
    }

    public Cell getCellByCoordinates(int x, int y) {
        for (Cell deck : body) {
            if (deck.getX() == x && deck.getY() ==  y) return deck;
        }
        return null;
    }

    public isAlive getDamage(int x, int y) {
        Cell deck = getCellByCoordinates(x, y);
        this.health--;
        this.health = (this.health < 0) ? (0) : this.health;
        if (this.health > 0) {
            this.status = isAlive.INJURED;
            deck.setStatus(Cell.Status.DAMAGED_DECK);
        } else {
            this.status = isAlive.DEAD;
            for (Cell deadDeck : body) {
                deadDeck.setStatus(Cell.Status.DAMAGED_SHIP);
            }
        }
        return this.status;
    }

    public Set<Cell> getBody() {
        return body;
    }

    public String getName() {
        return name;
    }
}