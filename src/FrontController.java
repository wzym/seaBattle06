import Model.Game;
import View.ViewTmp;

public class FrontController {
    public static void main(String[] args) {
        Game game = new Game();

        ViewTmp view = new ViewTmp(game.getPlayer1().getField());
    }
}
