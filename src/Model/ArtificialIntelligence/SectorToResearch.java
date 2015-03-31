package Model.ArtificialIntelligence;
/**
 * —ектор - участок на модели пол€ непри€тел€ (игрока), пр€моугольник, стороны которого (!!) равны или меньше,
 * чем длина искомого корабл€. ѕодстал€ем корабль во все позиции сектора, сохран€ем соответствующую координату
 * во временные массивы по ’ и ”. «атем перебираем, случайно выбира€ их этих массивов координаты дл€ получени€
 * конкретной €чейки дл€ отстрела. ¬ыбранную координату в массив дл€ возвращени€ и удал€ем из рабочих массивов
 * ’ и ”. ≈сли пересекающихс€, то есть парных, координат не осталось, выбираем дл€ всех одиноких случайную подход€щую пару.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SectorToResearch {
    private int headX;                                  // левый верхний угол сектора - точка отсчЄта
    private int headY;
    private int height;                                 // высота сектора
    private int width;                                  // ширина сектора
    private int lengthOfLookedForShip;                  // длина искомого корабл€
    private List<Integer> possibleX = new ArrayList<Integer>();
    private List<Integer> possibleY = new ArrayList<Integer>();
    // коллекци€ вариантов, выбранных дл€ отстрела
    private List<VariantToShot> variantsToOut = new LinkedList<VariantToShot>();

    public SectorToResearch(int headX, int headY, int height, int width, int lengthOfLookedForShip) {
        this.headX = headX;
        this.headY = headY;
        this.height = height;
        this.width = width;
        this.lengthOfLookedForShip = lengthOfLookedForShip;
    }
    
    private void getAllPossibleVariants() {
        boolean isVariantsUseful = true;
        if (height == lengthOfLookedForShip) {  // ≈сли вмещаетс€ по высоте, подставл€ем по горизонтали,
            for (int x = 0; x < width; x++) {       // мен€€ X; провер€ем вмещаемость тела,
                for (int y = 0; y < lengthOfLookedForShip; y++) {   // расположенного вертикально Y
                    if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[headX + x][headY + y].
                            getCurrentStatus() == VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT) {
                        isVariantsUseful = false;
                    }
                }
                if (isVariantsUseful) possibleX.add(x + headX);     // сохран€ем годную координату, а не X
                isVariantsUseful = true;
            }
        }
        if (width == lengthOfLookedForShip) {
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < lengthOfLookedForShip; x++) {
                    if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[headX + x][headY + y].
                            getCurrentStatus() == VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT) {
                        isVariantsUseful = false;
                    }
                }
                if (isVariantsUseful) possibleY.add(y + headY);     // сохран€ем координату, а не рабочий Y
                isVariantsUseful = true;
            }
        }
    }

    private void selectVariantsFromAll() {
        while (!possibleX.isEmpty() && !possibleY.isEmpty()) {
            int keyX = (int) (Math.round(Math.random() * (possibleX.size() - 1)));
            int keyY = (int) (Math.round(Math.random() * (possibleY.size() - 1)));
            int x = possibleX.get(keyX);
            int y = possibleY.get(keyY);
            variantsToOut.add(ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x][y]);
            possibleX.remove(keyX);
            possibleY.remove(keyY);
        }

        while (!possibleY.isEmpty()) {  // на случай, если остались вертикальные варианты
            int x = (int) (headX + Math.round(Math.random() * (lengthOfLookedForShip - 1)));
            int key = (int) (Math.round(Math.random() * (possibleY.size() - 1)));
            int y = possibleY.get(key);
            variantsToOut.add(ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x][y]);
            possibleY.remove(key);
        }

        while (!possibleX.isEmpty()) {
            int y = (int) (headY + Math.round(Math.random() * (lengthOfLookedForShip - 1)));
            int key = (int) (Math.round(Math.random() * (possibleX.size() - 1)));
            int x = possibleX.get(key);
            variantsToOut.add(ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[x][y]);
            possibleX.remove(key);
        }
    }

    public List<VariantToShot> getVariantsToOut() {
        this.getAllPossibleVariants();
        this.selectVariantsFromAll();
        return variantsToOut;
    }
}
