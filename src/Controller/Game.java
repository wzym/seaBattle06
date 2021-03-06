package Controller;

import Model.ArtificialIntelligence.ArtificialIntelligence;
import Model.ArtificialIntelligence.VariantToShot;
import Model.OneCell;
import Model.OneCell.Status;
import Model.Player;
import Model.Ship;
import View.View;

public class Game {
    private Player player1 = new Player(false);
    private Player player2 = new Player(true);
    private View view = new View(this);

    public Game() throws InterruptedException {
        this.autoTurn();
    }

    private synchronized void autoTurn() throws InterruptedException {
        while (player1.isPlayerInGame() && player2.isPlayerInGame()) {
            Thread.sleep(500);
            VariantToShot turn = player2.makeFireAutomatically();
            Status resultOfFire = oneTurn(turn.getX(), turn.getY(), false);
            player2.getResultOfFire(resultOfFire);
            if (resultOfFire != Status.DAMAGED_DECK && resultOfFire != Status.DAMAGED_SHIP) {
                wait();
            }
        }
    }

    private Status oneTurn(int x, int y, boolean isItGamersTurn) {
        Player player = isItGamersTurn? player2 : player1;      // выбираем игрока, по полю которого совершён выстрел

        Status typeOfFiredArea = player.getFire(x, y);  // меняем статус ячейки, по которой стреляли, получаем новый

        if (isItGamersTurn) {       // закрашиваем соответствующую ячейку в соответствии с новым статусом
            view.paintCompCellByStatus(x, y, typeOfFiredArea);
        } else {
            view.paintCellByStatus(x, y, typeOfFiredArea);
        }

        switch (typeOfFiredArea) {
            case DAMAGED_DECK:
                Ship injuredShip = player.getShipByCoordinates(x, y);
                view.showMessage(injuredShip.getName() + " повреждён.");
                return typeOfFiredArea;
            case DAMAGED_SHIP:
                Ship deadShip = player.getShipByCoordinates(x, y);
                view.showMessage(deadShip.getName() + " утонул.");
                for (OneCell deck : deadShip.getBody()) {
                    if (isItGamersTurn) {
                        view.paintCompCellByStatus(deck.getX(), deck.getY(), deck.getStatus());
                    } else {
                        view.paintCellByStatus(deck.getX(), deck.getY(), deck.getStatus());
                    }
                }
                if (!player.isPlayerInGame()) view.showMessage("Кораблей больше нет.");
                return typeOfFiredArea;
            case DAMAGED_WATER:
                return typeOfFiredArea;
        }
        return typeOfFiredArea;
    }

    public synchronized void listener(EventOfViewAndController event) {
        switch (event.getKindOfEvent()) {
            case TURN:
                Status resultOfFire = oneTurn(event.getX(), event.getY(), true);
                if (resultOfFire != Status.DAMAGED_SHIP && resultOfFire != Status.DAMAGED_DECK) {
                    notify();
                }
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
                VariantToShot[][] field = ArtificialIntelligence.getGameBrain().getExploredFieldOfOpponent();
                for (int y = 1; y < 11; y++) {
                    for (int x = 1; x < 11; x++) {
                        if (field[x][y].getCurrentStatus() == VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT) {
                            System.out.println(field[x][y].getX() + " " + field[x][y].getY());
                        }
                    }
                }
                break;
        }
    }
}