package Model;

public class SingleGame {
    private static SingleGame theOneGame;
    public static SingleGame getTheOneGame() {
        if (null == theOneGame) theOneGame = new SingleGame();
        return theOneGame;
    }

    private int height;
    private int width;
}
