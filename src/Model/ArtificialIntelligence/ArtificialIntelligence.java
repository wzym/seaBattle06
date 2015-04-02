package Model.ArtificialIntelligence;

import Model.ConfigOfGame;
import Model.OneCell;

import java.util.*;

public class ArtificialIntelligence {
    /**
     * Синхронизацию используем, так как этот экземпляр будет заниматься как расстановкой кораблей,
     * так и выбором цели для стрельбы
     */
    public static synchronized ArtificialIntelligence getGameBrain() {
        if (null == gameBrain) gameBrain = new ArtificialIntelligence();
        return gameBrain;
    }
    private static ArtificialIntelligence gameBrain;        // попытка реализовать синглтон

    // коллекция всех возможных вариантов установки; много дополняется, поэтому LinkedList
    private List<VariantOfPosition> allPossibleVariantsOfPosition = new LinkedList<VariantOfPosition>();

    protected VariantToShot[][] exploredFieldOfOpponent = new VariantToShot
            [ConfigOfGame.get().width() + 2][ConfigOfGame.get().height() + 2];
    private List<VariantToShot> variantsToShot = new ArrayList<VariantToShot>();
    private VariantToShot currentTurn;
    private int gageOfFilterToFind;
    private HandledShip handledShip = null;

    /**
     * Разведанный флот неприятеля состоит из коллекции длин всех кораблей неприятеля.
     * По мере уничтожения этих кораблей каждая соответствующая длина удаляется из коллекции.
     */
    private List<Integer> exploredFleetOfOpponentByLength = new ArrayList<Integer>();

    {
        /**
         * Инициализируем модель поля противника, где на месте буферных ячеек будут варианты
         * "не для стрельбы".
         */
        for (int x = 0; x <= ConfigOfGame.get().width() + 1; x++) {
            exploredFieldOfOpponent[x][0] = new VariantToShot(x, 0, VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT);
            exploredFieldOfOpponent[x][ConfigOfGame.get().width() + 1] = new VariantToShot(
                    x, ConfigOfGame.get().width() + 1, VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT);
        }
        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {
            exploredFieldOfOpponent[0][y] = new VariantToShot(0, y, VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT);
            exploredFieldOfOpponent[ConfigOfGame.get().height() + 1][y] = new VariantToShot(
                    ConfigOfGame.get().height() + 1, y, VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT);
        }
        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {
            for (int x = 1; x <= ConfigOfGame.get().width(); x++) {
                exploredFieldOfOpponent[x][y] =
                        new VariantToShot(x, y, VariantToShot.PresumptiveStatus.CELL_TO_SHOT);
            }
        }

        /**
         * Инициализируем флот неприятеля: для каждого типа корабля сохраняем соответствующее количество
         * длин в коллекцию
         */
        for (int[] type : ConfigOfGame.get().configOfShips()) {
            for (int i = 0; i < type[1]; i++) {
                exploredFleetOfOpponentByLength.add(type[0]);
            }
        }

        gageOfFilterToFind = getLengthOfLargestShip();
    }

    /**
     * При вызове метода поле возможных вариантов очищается и вновь наполняется исчерпывающим количеством
     * всех возможных вариантов исходя из текущей диспозиции.
     * Пробегает по всем ячейкам поля. Если из текущей ячейки возможно отложить корабль заданной длины,
     * проверяет каждую ячейку, которую он будет занимать. При обнаружении занятой ячейки данный вариант
     * считается негодным и в итоговый массив не идёт.
     */
    private void setAllPossibleVariantsOfPosition(int length, OneCell[][] field) {
        this.allPossibleVariantsOfPosition.clear();
        boolean isVariantSuitable = true;   // меняется, как только проверка варианта выявляет, что он неприемлем

        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {
            for (int x = 1; x <= ConfigOfGame.get().width(); x++) {
                if (x <= ConfigOfGame.get().width() - length + 1) {     // пока не добрались до правого края
                    for (int i = 0; i < length; i++) {  // проверяем горизонтальное расположение
                        if (field[x + i][y].getStatus() != OneCell.Status.WATER) isVariantSuitable = false;
                    }
                    if (isVariantSuitable) allPossibleVariantsOfPosition.add(new VariantOfPosition(x, y, true));
                    isVariantSuitable = true;
                }

                if (y <= ConfigOfGame.get().height() - length + 1) {    // пока не добрались до нижнего края
                    for (int i = 0; i < length; i++) {  // проверяем вертикальную позицию
                        if (field[x][y + i].getStatus() != OneCell.Status.WATER) isVariantSuitable = false;
                    }
                    if (isVariantSuitable) allPossibleVariantsOfPosition.add(new VariantOfPosition(x, y, false));
                    isVariantSuitable = true;
                }
            }
        }
    }

    /**
     * Выбирает один случайный вариант из доступных.
     */
    public VariantOfPosition getOneVariant(int length, OneCell[][] field) {
        this.setAllPossibleVariantsOfPosition(length, field);
        int key = (int) Math.round(Math.random() * (this.allPossibleVariantsOfPosition.size() - 1));
        return this.allPossibleVariantsOfPosition.get(key);
    }

    /**
     * Ищем корабли по очереди от самого крупного к шлюпкам. Для этого разделим разведанное поле на сектора
     * стороной, равной длине самого длинного из неподбитых кораблей. В каждом из этих секторов количеством
     * выстрелов, равным длине корабля, можно либо попасть, либо удостовериться, что здесь этого кораблся нет.
     * При попадании и непотоплении запускаем метод fatality, который обеспечит добивание корабля.
     */
    private void setAllVariantsOfShotToFind() {
        variantsToShot.clear();
        int integerIterationToX = ConfigOfGame.get().width() / gageOfFilterToFind;
        int remainderIteratorToX = ConfigOfGame.get().width() - integerIterationToX * gageOfFilterToFind;
        int integerIterationToY = ConfigOfGame.get().height() / gageOfFilterToFind;
        int remainderIteratorToY = ConfigOfGame.get().height() - integerIterationToY * gageOfFilterToFind;

        for (int y = 0; y < integerIterationToY; y++) {
            for (int x = 0; x < integerIterationToX; x++) {
                List<VariantToShot> variantsToAddInCommonCollection = new SectorToResearch(
                        1 + x * gageOfFilterToFind, 1 + y * gageOfFilterToFind, gageOfFilterToFind, gageOfFilterToFind,
                        gageOfFilterToFind).getVariantsToOut();
                for (VariantToShot variantToShot : variantsToAddInCommonCollection) {
                    variantsToShot.add(variantToShot);
                }
            }
        }

        for (int y = 0; y < integerIterationToY; y++) {     // правая узкая полоска
            List<VariantToShot> variantsToAddInCommonCollection = new SectorToResearch(
                    1 + integerIterationToX * gageOfFilterToFind, 1 + y * gageOfFilterToFind,
                    gageOfFilterToFind, remainderIteratorToX, gageOfFilterToFind).getVariantsToOut();
            for (VariantToShot variantToShot : variantsToAddInCommonCollection) {
                variantsToShot.add(variantToShot);
            }
        }

        for (int x = 0; x < integerIterationToX; x++) {     // нижняя узкая полоска
            List<VariantToShot> variantsToAddInCommonCollection = new SectorToResearch(
                    1 + x * gageOfFilterToFind, 1 + integerIterationToY * gageOfFilterToFind,
                    remainderIteratorToY, gageOfFilterToFind, gageOfFilterToFind).getVariantsToOut();
            for (VariantToShot variantToShot : variantsToAddInCommonCollection) {
                variantsToShot.add(variantToShot);
            }
        }
        if (this.variantsToShot.isEmpty()) {
            this.gageOfFilterToFind--;
            this.setAllVariantsOfShotToFind();
        }
    }

    private int getLengthOfLargestShip() {
        int length = 0;
        for (Integer oneLength : exploredFleetOfOpponentByLength) {
            length = (oneLength > length)? oneLength : length;
        }
        return length;
    }

    /**
     * Контроллер для выбора одного варианта для стрельбы. Определяет, каким методом будем выбирать,
     * затем запускает этот метод.
     */
    public void formVariantToCurrentTurn() {
        VariantToShot variantToReturn;
        if (null != this.handledShip) {
            variantToReturn = this.handledShip.getVariantToOut();
        } else {
            if (this.variantsToShot.isEmpty()) setAllVariantsOfShotToFind();
            int key = (int) Math.round(Math.random() * (this.variantsToShot.size() - 1));
            variantToReturn = this.variantsToShot.get(key);
            this.variantsToShot.remove(key);
        }
        this.currentTurn = variantToReturn;
    }

    public VariantToShot getVariantToCurrentTurn() {
        if (this.currentTurn == null) this.formVariantToCurrentTurn();
        return this.currentTurn;
    }

    public void setCurrentTurn(OneCell.Status result) {
        this.currentTurn.setCurrentStatus(VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT);
        switch (result) {
            case DAMAGED_DECK:
                if (null == this.handledShip) {
                    this.handledShip = new HandledShip(this.currentTurn, false);
                } else {
                    this.handledShip.setResultOfChecking(VariantToShot.ResultOfShot.HIT);
                }
                break;
            case DAMAGED_SHIP:
                if (null != this.handledShip) {
                    this.handledShip.setResultOfChecking(VariantToShot.ResultOfShot.DEATH_HIT);
                } else {
                    this.handledShip = new HandledShip(this.currentTurn, true);

                }
                this.setAllVariantsOfShotToFind();
                this.handledShip = null;
                break;
            case DAMAGED_WATER:
                if (null != this.handledShip) this.handledShip.setResultOfChecking(VariantToShot.ResultOfShot.MISS);
                break;
        }
        formVariantToCurrentTurn();
    }

    public VariantToShot[][] getExploredFieldOfOpponent() {
        return exploredFieldOfOpponent;
    }
}