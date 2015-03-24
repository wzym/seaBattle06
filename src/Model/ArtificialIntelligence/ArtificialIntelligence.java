package Model.ArtificialIntelligence;

import Model.Game;
import Model.Cell;

import java.util.LinkedList;
import java.util.List;

public class ArtificialIntelligence {
    private static ArtificialIntelligence gameBrain;        // попытка реализовать синглтон
    private List<VariantOfPosition> allPossibleVariantsOfPosition;

    private ArtificialIntelligence() {

    }

    {
        allPossibleVariantsOfPosition = new LinkedList<VariantOfPosition>();
    }

    /**
     * Синхронизацию используем, так как этот экземпляр будет заниматься как расстановкой кораблей,
     * так и выбором цели для стрельбы
     */
    public static synchronized ArtificialIntelligence getGameBrain() {
        if (null == gameBrain) gameBrain = new ArtificialIntelligence();
        return gameBrain;
    }

    private void setAllPossibleVariantsOfPosition(int length, Cell[][] field) {
        this.allPossibleVariantsOfPosition.clear();
        boolean isVariantSuitable = true;

        for (int y = 1; y <= Game.HEIGHT; y++) {
            for (int x = 1; x <= Game.WIDTH; x++) {
                if (x <= Game.WIDTH - length + 1) {     // пока не добрались до правого края
                    for (int i = 0; i < length; i++) {  // проверяем горизонтальное расположение
                        if (field[x + i][y].getStatus() != Cell.Status.WATER) isVariantSuitable = false;
                    }
                    if (isVariantSuitable) allPossibleVariantsOfPosition.add(new VariantOfPosition(x, y, true));
                    isVariantSuitable = true;
                }

                if (y <= Game.HEIGHT - length + 1) {    // пока не добрались до нижнего края
                    for (int i = 0; i < length; i++) {  // проверяем вертикальную позицию
                        if (field[x][y + i].getStatus() != Cell.Status.WATER) isVariantSuitable = false;
                    }
                    if (isVariantSuitable) allPossibleVariantsOfPosition.add(new VariantOfPosition(x, y, false));
                    isVariantSuitable = true;
                }
            }
        }
    }

    public VariantOfPosition getOneVariant(int length, Cell[][] field) {
        this.setAllPossibleVariantsOfPosition(length, field);
        int key = (int) Math.round(Math.random() * (this.allPossibleVariantsOfPosition.size() - 1));
        return this.allPossibleVariantsOfPosition.get(key);
    }
}
