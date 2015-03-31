package Model.ArtificialIntelligence;

public class VariantOfPosition {
    private int xOfHead;
    private int yOfHead;
    private boolean isHorizontal;

    public VariantOfPosition(int xOfHead, int yOfHead, boolean isHorizontal) {
        this.xOfHead = xOfHead;
        this.yOfHead = yOfHead;
        this.isHorizontal = isHorizontal;
    }

    public int getXOfHead() {
        return xOfHead;
    }

    public int getYOfHead() {
        return yOfHead;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }
}
