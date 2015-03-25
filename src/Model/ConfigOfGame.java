package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigOfGame {
    private static volatile ConfigOfGame theOneConfig = null;
    public static ConfigOfGame getMe() {
        if (null == theOneConfig)  theOneConfig = new ConfigOfGame();
        return theOneConfig;
    }

    private int height = 10;
    private int width = 10;
    private int[][] configOfShips = new int[][] {
            {4, 1},     // четырёхпалубный - один
            {3, 2},
            {2, 3},
            {1, 4}      // шлюпок - четыре
    };
    private static List<String> namesForShip = new ArrayList<String>();

    {
        String[] names = new String[] {
                "Стремительный", "Коварный", "Летящий", "Упорный", "Леденящий", "Двенадцатый", "Контролирующий",
                "Подводный", "Невероятный", "Чёрный", "Гладкий", "Светящийся", "Разящий", "Бездомный", "Нескончаемый",
                "Бесшовный", "Крылатый", "Пронзительный", "Центральный", "Говорящий", "Стрела", "Обходящий",
                "Прохладный", "Северный"
        };
        Collections.addAll(namesForShip, names);
    }

    public static String getRandomNameForShip() {
        int key = (int) Math.round(Math.random() * (namesForShip.size() - 1));
        if (key < 0) key = 0;
        return namesForShip.remove(key);
    }

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
