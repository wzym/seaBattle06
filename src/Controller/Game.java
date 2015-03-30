package Controller;

import Model.OneCell;
import Model.OneCell.Status;
import Model.Player;
import Model.Ship;
import View.View;

public class Game {
    private Player player1 = new Player();
    private Player player2 = new Player();
    private View view = new View(this);

    public Game() {

    }

    private void oneTurn(int x, int y, boolean isItGamersTurn) {
        Player player = isItGamersTurn? player2 : player1;
        Status typeOfFiredArea = player.getFire(x, y);
        switch (typeOfFiredArea) {
            case DECK:
                break;
            case WATER:
                break;
            case BUFFER:
                break;
            case DAMAGED_DECK:
                Ship injuredShip = player.getShipByCoordinates(x, y);
                view.showMessage(injuredShip.getName() + " повреждён.");
                break;
            case DAMAGED_SHIP:
                Ship deadShip = player.getShipByCoordinates(x, y);
                view.showMessage(deadShip.getName() + " утонул.");
                for (OneCell deck : deadShip.getBody()) {
                    view.paintCompCellByStatus(deck.getX(), deck.getY(), deck.getStatus());
                }
                if (!player.isPlayerInGame()) view.showMessage("Кораблей больше нет.");
                break;
            case DAMAGED_WATER:
                break;
        }
        view.paintCompCellByStatus(x, y, typeOfFiredArea);
    }

    public void listener(EventOfViewAndController event) {
        switch (event.getKindOfEvent()) {
            case TURN:
                oneTurn(event.getX(), event.getY(), true);
                break;
            case START:
                for (Ship ship : player1.getFleet().values()) {
                    for (OneCell deck : ship.getBody()) {
                        view.paintCellByStatus(deck.getX(), deck.getY(), deck.getStatus());
                    }
                }
                break;
            case SHOW_COMP:
                for (Ship ship : player2.getFleet().values()) {
                    for (OneCell deck : ship.getBody()) {
                        view.paintCompCellByStatus(deck.getX(), deck.getY(), deck.getStatus());
                    }
                }
        }
    }
}