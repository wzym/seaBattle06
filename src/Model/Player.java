package Model;

import java.util.*;
import Model.ArtificialIntelligence.ArtificialIntelligence;
import Model.ArtificialIntelligence.VariantOfPosition;
import View.View;

public class Player {
    private Cell[][] field;
    private HashMap<String, Ship> fleet;

    {
        this.field = new Cell[ConfigOfGame.get().width() + 2]
                [ConfigOfGame.get().height() + 2]; // +2 - для установки буфера по периметру
        this.fleet = new HashMap<String, Ship>();
    }

    public Player() {
        this.setWaterAndBuffer();
        this.setShipsAutomatically();

    }

    private void setWaterAndBuffer() {
        for (int i = 1; i <= ConfigOfGame.get().height(); i++) {    // инициализация поля, устанавливаем ячейки с водой
            for (int j = 1; j <= ConfigOfGame.get().width(); j++) {
                this.field[j][i] = new Cell(j, i, Cell.Status.WATER);
            }
        }
        for (int i = 0; i < ConfigOfGame.get().height() + 2; i++) {
            this.field[0][i] = new Cell(0, i, Cell.Status.BUFFER);     // устанавливаем левый буфер
            this.field[ConfigOfGame.get().width() + 1][i] =
                    new Cell(ConfigOfGame.get().width() + 1, i, Cell.Status.BUFFER);         // правый
        }
        for (int i = 0; i < ConfigOfGame.get().width() + 2; i++) {
            this.field[i][0] = new Cell(i, 0, Cell.Status.BUFFER);     // верхний буфер
            this.field[i][ConfigOfGame.get().height() + 1] =
                    new Cell(i, ConfigOfGame.get().height() + 1, Cell.Status.BUFFER);     // нижний
        }
    }

    private void setShipsAutomatically() {
        for (int[] configOfShip : ConfigOfGame.get().configOfShips()) {
            for (int i = 0; i < configOfShip[1]; i++) {
                VariantOfPosition var = ArtificialIntelligence.getGameBrain().
                        getOneVariant(configOfShip[0], this.field);
                this.setOneShip(ConfigOfGame.nameForShip(), var.getxOfHead(),
                        var.getyOfHead(), configOfShip[0], var.isHorizontal());
            }
        }
    }

    private void setOneShip(String name, int xOfHead, int yOfHead, int length, boolean isHorizontal) {
        this.fleet.put(name, new Ship());               // добавляем именованный корабль во флот
        for (int i = 0; i < length; i++) {
            this.field[xOfHead][yOfHead].setStatus(Cell.Status.DECK);       // отмечает на поле тело корабля
            this.fleet.get(name).getBody().add(field[xOfHead][yOfHead]);    // сохраняем эти ячейки в массив как палубы
            if (isHorizontal) { ++xOfHead; }
            else { ++yOfHead; }
        }
        if (isHorizontal) {
            for (int i = 0; i < length + 2; i++) {
                this.field[xOfHead - i][yOfHead - 1].setStatus(Cell.Status.BUFFER); // буфер сверху
                this.field[xOfHead - i][yOfHead + 1].setStatus(Cell.Status.BUFFER); // буфер снизу
            }
            this.field[xOfHead - 1 - length][yOfHead].setStatus(Cell.Status.BUFFER);    // буфер перед головой
            this.field[xOfHead][yOfHead].setStatus(Cell.Status.BUFFER);                 // буфер в хвосте
        }
        else {
            for (int i = 0; i < length + 2; i++) {
                this.field[xOfHead - 1][yOfHead - i].setStatus(Cell.Status.BUFFER);     // буфер слева
                this.field[xOfHead + 1][yOfHead - i].setStatus(Cell.Status.BUFFER);     // справа
            }
            this.field[xOfHead][yOfHead - length - 1].setStatus(Cell.Status.BUFFER);    // выше головы
            this.field[xOfHead][yOfHead].setStatus(Cell.Status.BUFFER);                 // ниже хвоста
        }
    }

    public void getFire(int x, int y) {
        switch (this.field[x][y].getStatus()) {
            case DECK:
                this.field[x][y].setStatus(Cell.Status.DAMAGED_DECK);

                break;
            case WATER:
                this.field[x][y].setStatus(Cell.Status.DAMAGED_WATER);
                break;
            case BUFFER:
                this.field[x][y].setStatus(Cell.Status.DAMAGED_WATER);
                break;
        }
    }

    public Cell[][] getField() {
        return field;
    }

    public Map<String, Ship> getFleet() {
        return fleet;
    }
}