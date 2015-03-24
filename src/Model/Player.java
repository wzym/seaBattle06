package Model;

import java.util.*;
import Model.ArtificialIntelligence.ArtificialIntelligence;
import Model.ArtificialIntelligence.VariantOfPosition;

public class Player {
    private Cell[][] field;
    private Map<String, Ship> fleet;

    {
        this.field = new Cell[Game.WIDTH + 2][Game.HEIGHT + 2]; // +2 - для установки буфера по периметру
        this.fleet = new HashMap<String, Ship>();
    }

    public Player() {
        this.setWaterAndBuffer();
        this.setShipsAutomatically();
    }

    private void setWaterAndBuffer() {
        for (int i = 1; i <= Game.HEIGHT; i++) {         // инициализация поля, устанавливаем ячейки с водой
            for (int j = 1; j <= Game.WIDTH; j++) {
                this.field[j][i] = new Cell(Cell.Status.WATER);
            }
        }
        for (int i = 0; i < Game.HEIGHT + 2; i++) {
            this.field[0][i] = new Cell(Cell.Status.BUFFER);     // устанавливаем левый буфер
            this.field[Game.WIDTH + 1][i] = new Cell(Cell.Status.BUFFER);         // правый
        }
        for (int i = 0; i < Game.WIDTH + 2; i++) {
            this.field[i][0] = new Cell(Cell.Status.BUFFER);     // верхний буфер
            this.field[i][Game.HEIGHT + 1] = new Cell(Cell.Status.BUFFER);     // нижний
        }
    }


    private void setShipsAutomatically() {
        for (int[] configOfShip : Game.configOfShips) {
            for (int i = 0; i < configOfShip[1]; i++) {
                VariantOfPosition var = ArtificialIntelligence.getGameBrain().getOneVariant(configOfShip[0], this.field);
                this.setOneShip(Game.getRandomNameForShip(), var.getxOfHead(), var.getyOfHead(), configOfShip[0], var.isHorizontal());
            }
        }

        //VariantOfPosition variant = ArtificialIntelligence.getGameBrain().getOneVariant(4, this.field);
        //this.setOneShip("DiSnuff", variant.getxOfHead(), variant.getyOfHead(), 4, variant.isHorizontal());
    }

    public void setOneShip(String name, int xOfHead, int yOfHead, int length, boolean isHorizontal) {
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

    public Cell[][] getField() {
        return field;
    }
}