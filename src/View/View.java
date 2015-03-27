package View;

import Controller.Game;
import Model.Cell;
import Model.Cell.Status;
import Model.ConfigOfGame;
import Model.Ship;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;

public class View extends JFrame {
    private JFrame frame = new JFrame("Морской бой 0.6");

    private Game game;  // пока игру будем хранить в этом классе в этом поле

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
    private JLabel currentInformation = new JLabel("Здесь будет ценная информация");
    private JToggleButton showComputerShips = new JToggleButton("Показать/скрыть корабли компьютера");

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
                reviewFieldsOfGamer();
                if (showComputerShips.isSelected()) reviewFieldsOfComputer();
            }
        });
        showComputerShips.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (game != null) {
                    if (showComputerShips.isSelected()) {
                        reviewFieldsOfGamer();
                        reviewFieldsOfComputer();
                    } else {
                        paintAllComputerCells();
                    }
                }
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
        controller.add(currentInformation);
        controller.add(showComputerShips);

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
        cellsOfGamer[0][0].setEnabled(false);
        cellsOfComputer[0][0] = new JButton();
        cellsOfComputer[0][0].setEnabled(false);

        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {    // объявляем подписи вертикальной оси
            cellsOfGamer[0][y] = new JButton(String.valueOf(y));
            cellsOfGamer[0][y].setEnabled(false);
            cellsOfComputer[0][y] = new JButton(String.valueOf(y));
            cellsOfComputer[0][y].setEnabled(false);
        }
        for (int x = 1; x <= ConfigOfGame.get().width(); x++) {     // подписи горизонтальной оси
            cellsOfGamer[x][0] = new JButton(String.valueOf(numberToLetter[x - 1]));
            cellsOfGamer[x][0].setEnabled(false);
            cellsOfComputer[x][0] = new JButton(String.valueOf(numberToLetter[x - 1]));
            cellsOfComputer[x][0].setEnabled(false);
        }

        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {        // объявляем поле, устанавливая кнопки-ячейки
            for (int x = 1; x <= ConfigOfGame.get().width(); x++) {
                cellsOfGamer[x][y] = new JButton();
                cellsOfComputer[x][y] = new JButton();

                final int finalX = x;
                final int finalY = y;
                cellsOfComputer[x][y].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (game != null) {     // Нажатие на кнопки вызывает действие только после запуска игры
                            makeFire(finalX, finalY);
                        } else {
                            currentInformation.setText("Не кликай, игра ещё не запущена. Меню -> Игра -> Запустить игру");
                        }
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

    private void paintAllComputerCells() {
        for (int y = 1; y < cellsOfComputer.length; y++) {
            JButton[] jButtons = cellsOfComputer[y];
            for (int x = 1; x < jButtons.length; x++) {
                JButton jButton = jButtons[x];
                jButton.setBackground(Color.blue);
            }
        }
    }

    private void reviewFieldsOfGamer() {
        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {
            for (int x = 1; x <= ConfigOfGame.get().width(); x++) {
                cellsOfGamer[x][y].setBackground(
                        setColorByStatusOfCell(game.getPlayer1().getField()[x][y].getStatus())
                );
            }
        }
    }

    private void reviewFieldsOfComputer() {
        for (int y = 1; y <= ConfigOfGame.get().height(); y++) {
            for (int x = 1; x <= ConfigOfGame.get().width(); x++) {
                Status status = game.getPlayer2().getField()[x][y].getStatus();
                cellsOfComputer[x][y].setBackground(setColorByStatusOfCell(status));
                if (showComputerShips.isSelected()) {   // отображаем все поля, если такая настройка выбрана
                    cellsOfComputer[x][y].setBackground(setColorByStatusOfCell(status));
                } else {
                    if (status != Status.DECK) {       // в противном случае всё кроме неподбитых палуб
                        cellsOfComputer[x][y].setBackground(setColorByStatusOfCell(status));
                    }
                }
            }
        }
    }

    private Color setColorByStatusOfCell(Status status) {      // функция сопоставления статусов и цветов
        switch (status) {
            case WATER:
                return Color.blue;
            case DECK:
                return Color.black;
            case BUFFER:
                return Color.blue;
            case DAMAGED_WATER:
                return Color.cyan;
            case DAMAGED_DECK:
                return Color.red;
            case DAMAGED_SHIP:
                return Color.gray;
        }
        return null;
    }

    /**
     * На выбранной ячейке меняет цвет в зависимости от нового статуса. Если корабль подбит -
     * закрашивает все его палубы.
     * */
    private void makeFire(int x, int y) {
        Status typeOfFiredArea = game.getPlayer2().getFire(x, y);
        switch (typeOfFiredArea) {
            case DECK:
                break;
            case WATER:
                break;
            case BUFFER:
                break;
            case DAMAGED_DECK:
                Ship injuredShip = game.getPlayer2().getShipByCoordinates(x, y);
                currentInformation.setText(injuredShip.getName() + " повреждён.");
                break;
            case DAMAGED_SHIP:
                Ship deadShip = game.getPlayer2().getShipByCoordinates(x, y);
                currentInformation.setText(deadShip.getName() + " утонул.");
                for (Cell cell : deadShip.getBody()) {
                    cellsOfComputer[cell.getX()][cell.getY()].setBackground(setColorByStatusOfCell(cell.getStatus()));
                }
                if (!game.getPlayer2().isPlayerInGame()) currentInformation.setText("Кораблей больше нет.");
                break;

            case DAMAGED_WATER:
                break;
        }
        // закрашиваем ячейку в соответствующий цвет
        cellsOfComputer[x][y].setBackground(setColorByStatusOfCell(typeOfFiredArea));
    }
}