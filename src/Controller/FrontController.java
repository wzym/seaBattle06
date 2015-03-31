package Controller;

import View.View;

public class FrontController {
    public static void main(String[] args) {
        try {
            Game game = new Game();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //View view = new View();
    }
}
