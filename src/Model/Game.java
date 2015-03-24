package Model;

import java.util.ArrayList;
import java.util.List;

public class Game {
    public static int WIDTH;
    public static int HEIGHT;
    public int[][] configOfShips;
    private List<String> namesForShip;
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
                "Бесшовный", "Крылатый", "Пронзительный", "Центральный", "Говорящий", "Стрела", "Обходящий"
        };
        namesForShip = new ArrayList<String>();
        for (String name : names) {
            namesForShip.add(name);
        }
        player1 = new Player();
        player2 = new Player();
    }

    public Game() {
        player1.setOneShip(getRandomNameForShip(), 1, 1, 4, true);

    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public String getRandomNameForShip() {
        int key = (int) Math.round(Math.random() * namesForShip.size());
        return namesForShip.remove(key);
    }
}
