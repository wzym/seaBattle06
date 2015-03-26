package View;

import Controller.Game;
import Model.Cell;
import Model.ConfigOfGame;
import Model.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class View extends JFrame {
    private JFrame frame = new JFrame("Морской бой 0.6");

    private Game game;  // пока игру будем хранить в этом классе

    private JMenuBar menuBar = new JMenuBar();
    private JMenu menuGame = new JMenu("Игра");
    private JMenu menuAbout = new JMenu("Справка");
    private JMenuItem menuItemRestart = new JMenuItem("Запустить игру");
    private JMenuItem menuItemExit = new JMenuItem("Выход");

    private Container bothFields = new Container();
    private JPanel fieldOfGamer =
            new JPanel(new GridLayout(ConfigOfGame.get().width() + 1, ConfigOfGame.get().height() + 1));
    private JPanel fieldOfComputer =
            new JPanel(new GridLayout(ConfigOfGame.get().width() + 1, ConfigOfGame.get().height() + 1));
    private JPanel controller = new JPanel();

    private JButton[][] cellsOfGamer;       // массивы кнопок для отображения поля
    private JButton[][] cellsOfComputer;

    private char[] numberToLetter;


    {
        frame.setLayout(new BorderLayout());    // композиция окна
        frame.setSize(1080, 810);               // размеры окна
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  // закрываем приложении при закрытии окна

        menuItemExit.addActionListener(new ActionListener() {       // вешаем слушатели на менюшки
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuItemRestart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (null == game) game = new Game();
                arrangeShips(game.getPlayer1().getFleet());
            }
        });

        menuGame.add(menuItemRestart);      // формируем панель меню
        menuGame.add(menuItemExit);
        menuBar.add(menuGame);
        menuBar.add(menuAbout);
        frame.add(menuBar, BorderLayout.NORTH);     // добавляем меню наверх

        fieldOfGamer.setSize(500, 500);
        fieldOfComputer.setSize(500, 500);
        fieldOfGamer.setLocation(20, 20);
        fieldOfComputer.setLocation(540, 20);
        bothFields.setLayout(null);                 // не используем композиционную разметку, расположение - вручную
        bothFields.add(fieldOfGamer);
        bothFields.add(fieldOfComputer);

        numberToLetter = new char[]      // массив для вывода координаты "x" в буквенном кириллическом виде
                {'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ж', 'З', 'И', 'К', 'Л', 'М', 'Н', 'О', 'П',
                        'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Ы', 'Э', 'Ю', 'Я'};

        cellsOfGamer = new JButton[ConfigOfGame.get().height() + 1][ConfigOfGame.get().width() + 1];
        cellsOfComputer = new JButton[ConfigOfGame.get().height() + 1][ConfigOfGame.get().width() + 1];

        initializationOfFields();

        frame.add(bothFields, BorderLayout.CENTER); // добавляем контейнер с двумя игровыми полями
        frame.add(controller, BorderLayout.SOUTH);

        frame.setVisible(true);         // отображаем окно
    }

    public View() {

    }

    private void initializationOfFields() {
        cellsOfGamer[0][0] = new JButton();     // объявляем угловые неиспользуемые ячейки
        cellsOfComputer[0][0] = new JButton();

        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {    // объявляем подписи вертикальной оси
            cellsOfGamer[0][y] = new JButton(String.valueOf(y));
            cellsOfComputer[0][y] = new JButton(String.valueOf(y));
        }
        for (int x = 1; x <= ConfigOfGame.get().width(); x++) {     // подписи горизонтальной оси
            cellsOfGamer[x][0] = new JButton(String.valueOf(numberToLetter[x - 1]));
            cellsOfComputer[x][0] = new JButton(String.valueOf(numberToLetter[x - 1]));
        }

        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {        // объявляем поле
            for (int x = 1; x <= ConfigOfGame.get().width(); x++) {
                cellsOfGamer[x][y] = new JButton();
                cellsOfComputer[x][y] = new JButton();

                final int finalX = x;
                final int finalY = y;
                cellsOfComputer[x][y].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        makeFire(finalX, finalY);
                    }
                });

                cellsOfGamer[x][y].setBackground(Color.blue);
                cellsOfComputer[x][y].setBackground(Color.blue);
            }
        }

        for (int y = 0; y <= ConfigOfGame.get().height(); y++) {    // добавляем полученный массив кнопок на
            for (int x = 0; x <= ConfigOfGame.get().width(); x++) { // соответствующие ячейки
                fieldOfGamer.add(cellsOfGamer[x][y]);
                fieldOfComputer.add(cellsOfComputer[x][y]);
            }
        }
    }

    private void arrangeShips(Map<String, Ship> fleet) {
        for (Ship ship : fleet.values()) {
            for (Cell cell : ship.getBody()) {
                cellsOfGamer[cell.getX()][cell.getY()].setBackground(Color.black);
            }
        }
    }

    private void makeFire(int x, int y) {
        switch (game.getPlayer2().getFire(x, y)) {
            case DAMAGED_WATER:
                cellsOfComputer[x][y].setBackground(Color.cyan);
                break;
            case DAMAGED_DECK:
                cellsOfComputer[x][y].setBackground(Color.orange);
                break;
            case DAMAGED_SHIP:
                cellsOfComputer[x][y].setBackground(Color.gray);
                break;
        }
    }
}