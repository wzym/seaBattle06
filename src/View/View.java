package View;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

import Model.Cell;
import Model.ConfigOfGame;
import Model.Player;

public class View extends JFrame {
    private final JFrame frame = new JFrame("Морской бой 06");
    private JButton[][] fieldOfFirstPlayer = new JButton[ConfigOfGame.get().height() + 1][ConfigOfGame.get().width() + 1];
    private JButton[][] fieldOfSecondPlayer = new JButton[ConfigOfGame.get().height() + 1][ConfigOfGame.get().width() + 1];
    private char[] numberToLetter;

    {
        this.numberToLetter = new char[]      // массив для вывода координаты "x" в буквенном кириллическом виде
                {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'К', 'Л', 'М', 'Н', 'О', 'П',
                        'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Ы', 'Э', 'Ю', 'Я'};
    }

    public View(Cell[][] data1, Cell[][] data2) {

        frame.setLayout(null);
        frame.setSize(1100, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(11, 11));
        JPanel panel2 = new JPanel(new GridLayout(11, 11));
        panel.setLocation(50, 50);
        panel2.setLocation(550, 50);
        panel.setSize(480, 480);
        panel2.setSize(480, 480);

        this.initField(fieldOfFirstPlayer, panel, data1);
        this.initField(fieldOfSecondPlayer, panel2, data2);

        frame.add(panel);
        frame.add(panel2);

        frame.setVisible(true);
    }

    private void initField(JButton[][] field, JPanel panel, Cell[][] dateToView) {
        field[0][0] = new JButton();

        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {
            field[0][y] = new JButton(String.valueOf(y));
        }
        for (int x = 1; x <= ConfigOfGame.get().width(); x++) {
            field[x][0] = new JButton(String.valueOf(numberToLetter[x - 1]));
        }

        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {
            for (int x = 1; x <= ConfigOfGame.get().width(); x++) {
                field[x][y] = new JButton();

                switch (dateToView[x][y].getStatus()) {
                    case WATER:
                        field[x][y].setBackground(Color.blue);
                        break;
                    case DECK:
                        field[x][y].setBackground(Color.black);
                        break;
                    case BUFFER:
                        field[x][y].setBackground(Color.blue);
                        break;
                    case DAMAGED_WATER:
                        field[x][y].setBackground(Color.cyan);
                        break;
                    case DAMAGED_DECK:
                        field[x][y].setBackground(Color.orange);
                        break;
                    case DAMAGED_SHIP:
                        field[x][y].setBackground(Color.red);
                        break;
                }
            }
        }

        for (int y = 0; y <= ConfigOfGame.get().height(); y++) {
            for (int x = 0; x <= ConfigOfGame.get().width(); x++) {
                panel.add(field[x][y]);
            }
        }
    }

    public void setFire(int x, int y, JButton[][] field) {
        field[x][y].setBackground(Color.red);
    }
}