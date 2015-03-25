package Controller;

import Model.Player;
import View.View;

public class Game {
    private Player player1 = new Player();
    private Player player2 = new Player();

    public static void main(String[] args) {
        Game game = new Game();

        View view1 = new View(game.getPlayer1().getField(), game.getPlayer2().getField());
    }

    public Player getPlayer1() {
        return player1;
    }
    public Player getPlayer2() {
        return player2;
    }
}