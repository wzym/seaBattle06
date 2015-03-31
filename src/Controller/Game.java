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
        while (player1.isPlayerInGame()) {
            for (int i = 1; i <= 10; i++) {
                for (int j = 1; j <= 10; j++) {
                    VariantToShot variantToShotNow = ArtificialIntelligence.getGameBrain().getOneVariantOfShot();
                    oneTurn(variantToShotNow.getX(), variantToShotNow.getY(), false);
                    Thread.sleep(500);
                }
            }
        }

    }

    private Status oneTurn(int x, int y, boolean isItGamersTurn) {
        Player player = isItGamersTurn? player2 : player1;      // выбираем, игрока, который совершил ход

        Status typeOfFiredArea = player.getFire(x, y);

        if (isItGamersTurn) {       // закрашиваем соответствующую ячейку
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