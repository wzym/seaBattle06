package View;

import javax.swing.*;
import java.awt.*;
import Model.Cell;
import Model.ConfigOfGame;

public class View extends JFrame {
    private Cell[][] dataOfFirstPlayer;
    private Cell[][] dataOfSecondPlayer;
    private String[] numberToLetter;

    {
        this.numberToLetter = new String[]      // массив для вывода координаты "x" в буквенном кириллическом виде
                {"~А~", "~Б~", "~В~", "~Г~", "~Д~", "~Е~", "~Ж~", "~З~", "~И~", "~К~", "Л"};
    }

    public View(Cell[][] data1, Cell[][] data2) {
        this.dataOfFirstPlayer = data1;
        this.dataOfSecondPlayer = data2;
        final JFrame frame = new JFrame("Морской бой 06");
        frame.setLayout(null);
        frame.setSize(700, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(10, 10));
        JPanel panel2 = new JPanel(new GridLayout(10, 10));
        panel.setLocation(50, 50);
        panel2.setLocation(300, 50);
        panel.setSize(200, 200);
        panel2.setSize(200, 200);

        panel2.setBackground(Color.green);

        JButton[][] data = new JButton[ConfigOfGame.getMe().getHeight() + 1][ConfigOfGame.getMe().getWidth() + 1];
        for (int y = 1; y <= ConfigOfGame.getMe().getHeight(); y++) {
            for (int x = 1; x <= ConfigOfGame.getMe().getWidth(); x++) {
                data[x][y] = new JButton();
                switch (this.dataOfFirstPlayer[x][y].getStatus()) {
                    case WATER:
                        data[x][y].setBackground(Color.blue);
                        break;
                    case DECK:
                        data[x][y].setBackground(Color.black);
                        break;
                    case BUFFER:
                        data[x][y].setBackground(Color.blue);
                        break;
                }
                data[x][y].setVisible(true);
                panel.add(data[x][y]);
            }
        }

        JButton[][] field2 = new JButton[ConfigOfGame.getMe().getHeight() + 1][ConfigOfGame.getMe().getWidth() + 1];
        for (int y = 1; y <= ConfigOfGame.getMe().getHeight(); y++) {
            for (int x = 1; x <= ConfigOfGame.getMe().getWidth(); x++) {
                field2[x][y] = new JButton();
                switch (this.dataOfSecondPlayer[x][y].getStatus()) {
                    case WATER:
                        field2[x][y].setBackground(Color.blue);
                        break;
                    case DECK:
                        field2[x][y].setBackground(Color.black);
                        break;
                    case BUFFER:
                        field2[x][y].setBackground(Color.blue);
                        break;
                }
                field2[x][y].setVisible(true);
                panel2.add(field2[x][y]);
            }
        }



        frame.add(panel);
        frame.add(panel2);

        frame.setVisible(true);

    }


}
