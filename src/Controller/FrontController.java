package Controller;

import View.View;

public class FrontController {
    public static void main(String[] args) {
        Game game = new Game();
        View view1 = new View(game.getPlayer1().getField(), game.getPlayer2().getField());
    }
}
