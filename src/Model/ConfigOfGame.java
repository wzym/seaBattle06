package Model;

public class ConfigOfGame {
    private static volatile ConfigOfGame theOneConfig = null;

    private ConfigOfGame() {}

    public static ConfigOfGame getTheOneConfig() {
        if (null == theOneConfig)  theOneConfig = new ConfigOfGame();
        return theOneConfig;
    }

    private int height = 10;
    private int width = 10;
    private int[][] configOfShips = new int[][] {
            {4, 1},
            {3, 2},
            {2, 3},
            {4, 1}
    };

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int[][] getConfigOfShips() {
        return configOfShips;
    }
}
