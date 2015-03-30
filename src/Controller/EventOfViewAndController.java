package Controller;

public class EventOfViewAndController {
    private KindOfEvent kindOfEvent;
    private int x;
    private int y;

    public EventOfViewAndController(KindOfEvent kindOfEvent) {
        this.kindOfEvent = kindOfEvent;
    }

    public enum KindOfEvent {
        START, GAME_CONTROL, TURN, SHOW_COMP
    }

    public KindOfEvent getKindOfEvent() {
        return kindOfEvent;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}