package Model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private Cell[][] field;
    private List<Ship> fleet;

    {
        field = new Cell[Game.WIDTH][Game.HEIGHT];
        fleet = new ArrayList<Ship>();
    }

    private void setField() {

    }

    private void setShipsAutomatically() {

    }
}