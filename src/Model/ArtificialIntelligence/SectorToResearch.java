package Model.ArtificialIntelligence;
/**
 * Сектор - квадрат на модели поля неприятеля, с шириной, равной ширине искомого корабля.
 * Мы подставляем искомый корабль во все возможные позиции и проверяем, мог ли он там быть.
 * Если мог, то не меняющуюся координату этого кораблся (для вертикальной позиции - x, для горизонтальной - y
 * сохраняем как требующую проверки. Затем выбираем случайно из этих координат x и y и получаем таким образом
 * координату для отстрела. Соответственно, эти выбранные координаты больше в проверке не нуждаются.
 * Так перебираем эти координаты, пока не закончится x или y (в нашем случае проверяется изначально по x).
 *
 * Так как класс служит как метод, вызывать экземпляры планируем анонимно.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SectorToResearch {
    private int headX;                                  // левый верхний угол сектора - точка отсчёта
    private int headY;
    private int height;                                 // высота сектора
    private int width;                                  // ширина сектора
    private int lengthOfLookedForShip;                  // длина искомого корабля
    private List<Integer> possibleX = new ArrayList<Integer>();
    private List<Integer> possibleY = new ArrayList<Integer>();
    // коллекция вариантов, выбранных для отстрела
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
        if (height == lengthOfLookedForShip) {  // Если вмещается по высоте, подставляем по горизонтали,
            for (int x = 0; x < width; x++) {       // меняя X; проверяем вмещаемость тела,
                for (int y = 0; y < lengthOfLookedForShip; y++) {   // расположенного вертикально Y
                    if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[headX + x][headY + y].
                            getCurrentStatus() == VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT) {
                        isVariantsUseful = false;
                    }
                }
                if (isVariantsUseful) possibleX.add(x + headX);     // сохраняем годную координату, а не X
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
                if (isVariantsUseful) possibleY.add(y + headY);     // сохраняем координату, а не рабочий Y
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
