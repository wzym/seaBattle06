package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import Controller.Game;
import Model.Cell;
import Model.ConfigOfGame;

public class View extends JFrame {
    private final JFrame frame = new JFrame("Морской бой 06");
    private JMenuBar menuBar = new JMenuBar();
    private JMenu game = new JMenu("Игра");
    private JMenu about = new JMenu("Справка");
    private JMenuItem restart = new JMenuItem("Запустить игру");
    private JMenuItem exit = new JMenuItem("Выход");
    private JButton[][] fieldOfFirstPlayer;
    private JButton[][] fieldOfSecondPlayer;
    private char[] numberToLetter;

    {
        game.add(restart);
        game.add(exit);
        menuBar.add(game);
        menuBar.add(about);

        fieldOfFirstPlayer = new JButton[ConfigOfGame.get().height() + 1][ConfigOfGame.get().width() + 1];
        fieldOfSecondPlayer =  new JButton[ConfigOfGame.get().height() + 1][ConfigOfGame.get().width() + 1];
        this.numberToLetter = new char[]      // массив для вывода координаты "x" в буквенном кириллическом виде
                {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'К', 'Л', 'М', 'Н', 'О', 'П',
                        'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Ы', 'Э', 'Ю', 'Я'};
    }

    public View(final Cell[][] data1, Cell[][] data2) {
        frame.setLayout(new BorderLayout());
        frame.setSize(1100, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final JPanel panel = new JPanel(new GridLayout(11, 11));
        final JPanel panel2 = new JPanel(new GridLayout(11, 11));
        panel.setLocation(20, 20);
        panel2.setLocation(520, 20);
        panel.setSize(480, 480);
        panel2.setSize(480, 480);

        restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fieldOfFirstPlayer[3][4].setBackground(Color.red);
            }
        });
        about.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.initFieldByData(fieldOfFirstPlayer, panel, data1);
        this.initFieldByData(fieldOfSecondPlayer, panel2, data2);

        frame.add(menuBar, BorderLayout.NORTH);

        Container container = getContentPane();
        container.setLayout(null);
        container.add(panel);
        container.add(panel2);

        frame.add(container, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private void initFieldByData(JButton[][] field, JPanel panel, Cell[][] dateToView) {
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

                final int finalX = x;
                final int finalY = y;
                field[x][y].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        fieldOfFirstPlayer[finalX][finalY].setBackground(Color.red);
                    }
                });
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