import Model.Game;
import View.View;
import View.ViewTmp;

public class FrontController {
    public static void main(String[] args) {
        Game game = new Game();

        //ViewTmp view = new ViewTmp(game.getPlayer1().getField());
        View view1 = new View(game.getPlayer1().getField(), game.getPlayer2().getField());
    }
}
