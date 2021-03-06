package Model;

/**
 * Попытка реализовать настройки игры как синглтон.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConfigOfGame {     // реализация синглтона
    private static ConfigOfGame theOneConfig = null;
    public static ConfigOfGame get() {
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
    private static List<String> namesForShip = new ArrayList<String>(24);

    {   // формирование коллекции имён кораблей
        String[] names = new String[] {
                "Стремительный", "Коварный", "Летящий", "Упорный", "Леденящий", "Двенадцатый", "Контролирующий",
                "Подводный", "Невероятный", "Чёрный", "Гладкий", "Светящийся", "Разящий", "Бездомный", "Нескончаемый",
                "Бесшовный", "Крылатый", "Пронзительный", "Центральный", "Говорящий", "Стрела", "Обходящий",
                "Прохладный", "Северный"
        };
        Collections.addAll(namesForShip, names);
    }

    /**
     * Возвращает выбранное случайно из коллекции имя кораблся, удаляя его из коллекции для
     * уникальности имени
     * @return
     */
    public static String nameForShip() {
        int key = (int) Math.round(Math.random() * (namesForShip.size() - 1));
        if (key < 0) key = 0;
        return namesForShip.remove(key);
    }

    public int height() {
        return height;
    }

    public int width() {
        return width;
    }

    public int[][] configOfShips() {
        return configOfShips;
    }
}