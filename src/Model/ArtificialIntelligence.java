package Model;

import java.util.ArrayList;
import java.util.List;

public abstract class ArtificialIntelligence {
    private static List<VariantOfPosition> allPossibleVariantsOfPosition;

    static {
        allPossibleVariantsOfPosition = new ArrayList();
    }

    private static class VariantOfPosition {
        private int x;
        private int y;
        private boolean isHorizontal;

        private VariantOfPosition(int x, int y, boolean isHorizontal) {
            this.x = x;
            this.y = y;
            this.isHorizontal = isHorizontal;
        }
    }

    private static void getAllVariantsOfPosition() {


    }

    public static void getTrueRandomVariant() {
        getAllVariantsOfPosition();

    }
}
