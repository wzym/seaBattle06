package Model;
/**
 * Игрок имеет своё игровое поле - двумерный массив - [x][y] - ячеек и ассоциативную коллекцию
 * кораблей - ключ->значение. А также статус, который при поражении всех палуб всех кораблей
 * становится false - isPlayerInGame.
 */
import java.util.*;
import Model.ArtificialIntelligence.ArtificialIntelligence;
import Model.ArtificialIntelligence.VariantOfPosition;
import Model.ArtificialIntelligence.VariantToShot;

public class Player {
    private OneCell[][] field;
    private HashMap<String, Ship> fleet;
    private boolean isPlayerInGame;
    private boolean isThisPlayerComputer;

    {   // инициализуем массив поля и коллекцию кораблей; габариты поля - из конфигурации
        this.field = new OneCell[ConfigOfGame.get().width() + 2]
                [ConfigOfGame.get().height() + 2]; // +2 - для установки буфера по периметру
        this.fleet = new HashMap<String, Ship>();
        this.isPlayerInGame = true;
    }

    /**
     * При создании игрока инициализуем все ячейки поля, заливаем их водой.
     * Затем автоматически заполняем их кораблями.
     */
    public Player(boolean isThisPlayerComputer) {
        this.isThisPlayerComputer = isThisPlayerComputer;
        this.setWaterAndBuffer();
        this.setShipsAutomatically();
    }

    /**
     * Каждый корабль имеет вокрук себя буфер. В первую очередь служит для формирования
     * корректных вариантов расстановки. Кроме того буфер обрамляет игровое поле.
     */
    private void setWaterAndBuffer() {
        for (int i = 1; i <= ConfigOfGame.get().height(); i++) {    // инициализация поля, устанавливаем ячейки с водой
            for (int j = 1; j <= ConfigOfGame.get().width(); j++) {
                this.field[j][i] = new OneCell(j, i, OneCell.Status.WATER);
            }
        }
        for (int i = 0; i <= ConfigOfGame.get().height() + 1; i++) {
            this.field[0][i] = new OneCell(0, i, OneCell.Status.BUFFER);     // устанавливаем левый буфер
            this.field[ConfigOfGame.get().width() + 1][i] =
                    new OneCell(ConfigOfGame.get().width() + 1, i, OneCell.Status.BUFFER);         // правый
        }
        for (int i = 1; i <= ConfigOfGame.get().width(); i++) {
            this.field[i][0] = new OneCell(i, 0, OneCell.Status.BUFFER);     // верхний буфер
            this.field[i][ConfigOfGame.get().height() + 1] =
                    new OneCell(i, ConfigOfGame.get().height() + 1, OneCell.Status.BUFFER);     // нижний
        }
    }

    /**
     * Перебираем конфигурацию кораблей, для каждого (по мере убывания палуб) формируем исчерпывающую
     * коллекцию вариантов расстановки, выбираем случайный вариант, и в соответствии с ним
     * выставляем корабль на поле
     */
    private void setShipsAutomatically() {
        for (int[] configOfShip : ConfigOfGame.get().configOfShips()) {
            for (int i = 0; i < configOfShip[1]; i++) {
                VariantOfPosition var = ArtificialIntelligence.getGameBrain().
                        getOneVariant(configOfShip[0], this.field);
                this.setOneShip(ConfigOfGame.nameForShip(), var.getXOfHead(),
                        var.getYOfHead(), configOfShip[0], var.isHorizontal());
            }
        }
    }

    /**
     * Выставляем корабль на поле, сохраняем соответствующим ячейкам статус палубы и добавляем их в
     * тело кораблся (ship.body). Отмечаем буфер вокруг кораблся.
     */
    private void setOneShip(String name, int xOfHead, int yOfHead, int length, boolean isHorizontal) {
        this.fleet.put(name, new Ship(name, length));               // добавляем именованный корабль во флот
        for (int i = 0; i < length; i++) {
            this.field[xOfHead][yOfHead].setStatus(OneCell.Status.DECK);       // отмечает на поле тело корабля
            this.fleet.get(name).getBody().add(field[xOfHead][yOfHead]);    // сохраняем эти ячейки в массив как палубы
            if (isHorizontal) { ++xOfHead; }
            else { ++yOfHead; }
        }
        if (isHorizontal) {
            for (int i = 0; i < length + 2; i++) {
                this.field[xOfHead - i][yOfHead - 1].setStatus(OneCell.Status.BUFFER); // буфер сверху
                this.field[xOfHead - i][yOfHead + 1].setStatus(OneCell.Status.BUFFER); // буфер снизу
            }
            this.field[xOfHead - 1 - length][yOfHead].setStatus(OneCell.Status.BUFFER);    // буфер перед головой
            this.field[xOfHead][yOfHead].setStatus(OneCell.Status.BUFFER);                 // буфер в хвосте
        }
        else {
            for (int i = 0; i < length + 2; i++) {
                this.field[xOfHead - 1][yOfHead - i].setStatus(OneCell.Status.BUFFER);     // буфер слева
                this.field[xOfHead + 1][yOfHead - i].setStatus(OneCell.Status.BUFFER);     // справа
            }
            this.field[xOfHead][yOfHead - length - 1].setStatus(OneCell.Status.BUFFER);    // выше головы
            this.field[xOfHead][yOfHead].setStatus(OneCell.Status.BUFFER);                 // ниже хвоста
        }
    }

    /**
     * Когда совершается выстрел по полю игрока, в зависимости от статуса ячейки, в которую попали,
     * меняется её статус. Если попадание в корабль - запускаем у этого корабля соответствующий метод.
     * Если корабль мёртв - проверяем, остались ли ещё корабли.
     */
    public OneCell.Status getFire(int x, int y) {
        OneCell.Status status = this.field[x][y].getStatus();
        if (isThisPlayerComputer) {

        }
        switch (status) {
            case DECK:
                this.field[x][y].setStatus(OneCell.Status.DAMAGED_DECK);
                Ship injuredShip = this.getShipByCoordinates(x, y);  // получаем поражённый корабль
                if (injuredShip.getDamage(x, y) == Ship.isAlive.DEAD) {
                    if (checkAmountOfShips() == 0) this.isPlayerInGame = false;
                }
                break;
            case WATER:
                this.field[x][y].setStatus(OneCell.Status.DAMAGED_WATER);
                break;
            case BUFFER:
                this.field[x][y].setStatus(OneCell.Status.DAMAGED_WATER);
                break;
        }
        return this.field[x][y].getStatus();
    }

    private int checkAmountOfShips() {  // для проверки, в игре ли ещё этот игрок
        int amountOfShips = 0;
        for (Ship ship : fleet.values()) {
            if (ship.getStatus() != Ship.isAlive.DEAD) amountOfShips++;
        }
        return amountOfShips;
    }

    public VariantToShot makeFireAutomatically() {
        return ArtificialIntelligence.getGameBrain().getOneVariantOfShot();
    }

    /**
     * Пробегаем по всему флоту, в каждом корабле вызываем метод поиска палубы по координатам.
     * Если таковая есть - возвращаем корабль.
     */
    public Ship getShipByCoordinates(int x, int y) {
        for (Ship ship : fleet.values()) {
            if (null != ship.getCellByCoordinates(x, y)) return ship;
        }
        return null;
    }

    public boolean isPlayerInGame() {
        return isPlayerInGame;
    }

    public HashMap<String, Ship> getFleet() {
        return fleet;
    }
}