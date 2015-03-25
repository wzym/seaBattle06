package Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Game {
    private static Game game;
    public static synchronized Game getGame() {
        if (null == game) game = new Game();
        return game;
    }
    public static int WIDTH;
    public static int HEIGHT;
    public static int[][] configOfShips;
    private static List<String> namesForShip;
    private Player player1;
    private Player player2;

    {       // конфигурация игры: размеры поля, флота, игроги
        WIDTH = 10;
        HEIGHT = 10;
        configOfShips = new int[][] {
                {4, 1},     // четырёхпалубных - один
                {3, 2},     // трёхпалубных - два
                {2, 3},     // двупалубных - три
                {1, 4}      // шлюпок - четыре
        };
        String[] names = new String[] {
                "Стремительный", "Коварный", "Летящий", "Упорный", "Леденящий", "Двенадцатый", "Контролирующий",
                "Подводный", "Невероятный", "Чёрный", "Гладкий", "Светящийся", "Разящий", "Бездомный", "Нескончаемый",
                "Бесшовный", "Крылатый", "Пронзительный", "Центральный", "Говорящий", "Стрела", "Обходящий",
                "Прохладный", "Северный"
        };
        namesForShip = new ArrayList<String>();
        Collections.addAll(namesForShip, names);
        player1 = new Player();
        player2 = new Player();
    }

    public Player getPlayer1() {
        return player1;
    }


    public static String getRandomNameForShip() {
        int key = (int) Math.round(Math.random() * (namesForShip.size() - 1));
        if (key < 0) key = 0;
        return namesForShip.remove(key);
    }
}