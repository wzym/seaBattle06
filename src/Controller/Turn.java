package Controller;

public class Turn extends EventOfViewAndController {
    public int x;
    private int y;

    public Turn(int x, int y) {
        super(KindOfEvent.TURN);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}