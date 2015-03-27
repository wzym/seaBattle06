package Model.ArtificialIntelligence;

import Model.ConfigOfGame;
import Model.Cell;

import java.util.LinkedList;
import java.util.List;

public class ArtificialIntelligence {
    private static ArtificialIntelligence gameBrain;        // попытка реализовать синглтон
    // коллекция всех возможных вариантов установки; много дополняется, поэтому LinkedList
    private List<VariantOfPosition> allPossibleVariantsOfPosition = new LinkedList<VariantOfPosition>();

    /**
     * Синхронизацию используем, так как этот экземпляр будет заниматься как расстановкой кораблей,
     * так и выбором цели для стрельбы
     */
    public static synchronized ArtificialIntelligence getGameBrain() {
        if (null == gameBrain) gameBrain = new ArtificialIntelligence();
        return gameBrain;
    }

    /**
     * При вызове метода поле возможных вариантов очищается и вновь наполняется исчерпывающим количеством
     * всех возможных вариантов исходя из текущей диспозиции.
     * Пробегает по всем ячейкам поля. Если из текущей ячейки возможно отложить корабль заданной длины,
     * проверяет каждую ячейку, которую он будет занимать. При обнаружении занятой ячейки данный вариант
     * считается негодным и в итоговый массив не идёт.
     */
    private void setAllPossibleVariantsOfPosition(int length, Cell[][] field) {
        this.allPossibleVariantsOfPosition.clear();
        boolean isVariantSuitable = true;   // меняется, как только проверка варианта выявляет, что он неприемлем

        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {
            for (int x = 1; x <= ConfigOfGame.get().width(); x++) {
                if (x <= ConfigOfGame.get().width() - length + 1) {     // пока не добрались до правого края
                    for (int i = 0; i < length; i++) {  // проверяем горизонтальное расположение
                        if (field[x + i][y].getStatus() != Cell.Status.WATER) isVariantSuitable = false;
                    }
                    if (isVariantSuitable) allPossibleVariantsOfPosition.add(new VariantOfPosition(x, y, true));
                    isVariantSuitable = true;
                }

                if (y <= ConfigOfGame.get().height() - length + 1) {    // пока не добрались до нижнего края
                    for (int i = 0; i < length; i++) {  // проверяем вертикальную позицию
                        if (field[x][y + i].getStatus() != Cell.Status.WATER) isVariantSuitable = false;
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
    public VariantOfPosition getOneVariant(int length, Cell[][] field) {
        this.setAllPossibleVariantsOfPosition(length, field);
        int key = (int) Math.round(Math.random() * (this.allPossibleVariantsOfPosition.size() - 1));
        return this.allPossibleVariantsOfPosition.get(key);
    }

    private void setAllVariantsOfShot() {

    }
}