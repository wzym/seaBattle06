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

    public int getxOfHead() {
        return xOfHead;
    }

    public int getyOfHead() {
        return yOfHead;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }
}
