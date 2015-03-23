package Model;

public class Ship {
    private int length;
    private boolean isHorizontal;
    private isAlive status;
    private Deck[] body;

    public class Deck {
        private int x;
        private int y;
        private isAlive status;
    }

    public enum isAlive {       // статус касается состояния палубы и состояния корабля
        ALIVE, INJURED, DEAD
    }

    public Ship(int length, boolean isHorizontal) {
        this.body = new Deck[length];       // инициализируем тело корабля как набор живых палуб
        for (Deck deck : this.body) {
            deck.status = isAlive.ALIVE;
        }
        this.isHorizontal = isHorizontal;
        this.status = isAlive.ALIVE;        // статус корабля - жив

    }

    public void setShip() {

    }
}