package Model.ArtificialIntelligence;
/**
 * ������ - ������� �� ������ ���� ���������� (������), �������������, ������� �������� (!!) ����� ��� ������,
 * ��� ����� �������� �������. ���������� ������� �� ��� ������� �������, ��������� ��������������� ����������
 * �� ��������� ������� �� � � �. ����� ����������, �������� ������� �� ���� �������� ���������� ��� ���������
 * ���������� ������ ��� ��������. ��������� ���������� � ������ ��� ����������� � ������� �� ������� ��������
 * � � �. ���� ��������������, �� ���� ������, ��������� �� ��������, �������� ��� ���� �������� ��������� ���������� ����.
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SectorToResearch {
    private int headX;                                  // ����� ������� ���� ������� - ����� �������
    private int headY;
    private int height;                                 // ������ �������
    private int width;                                  // ������ �������
    private int lengthOfLookedForShip;                  // ����� �������� �������
    private List<Integer> possibleX = new ArrayList<Integer>();
    private List<Integer> possibleY = new ArrayList<Integer>();
    // ��������� ���������, ��������� ��� ��������
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
        if (height == lengthOfLookedForShip) {  // ���� ��������� �� ������, ����������� �� �����������,
            for (int x = 0; x < width; x++) {       // ����� X; ��������� ����������� ����,
                for (int y = 0; y < lengthOfLookedForShip; y++) {   // �������������� ����������� Y
                    if (ArtificialIntelligence.getGameBrain().exploredFieldOfOpponent[headX + x][headY + y].
                            getCurrentStatus() == VariantToShot.PresumptiveStatus.CELL_NOT_TO_SHOT) {
                        isVariantsUseful = false;
                    }
                }
                if (isVariantsUseful) possibleX.add(x + headX);     // ��������� ������ ����������, � �� X
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
                if (isVariantsUseful) possibleY.add(y + headY);     // ��������� ����������, � �� ������� Y
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

        while (!possibleY.isEmpty()) {  // �� ������, ���� �������� ������������ ��������
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
