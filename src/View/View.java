package View;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import Model.Cell;
import Model.ConfigOfGame;
import Model.Game;

public class View {
    private Cell[][] dataToView;
    private String[] numberToLetter;

    {
        this.numberToLetter = new String[]      // массив для вывода координаты "x" в буквенном кириллическом виде
                {"~А~", "~Б~", "~В~", "~Г~", "~Д~", "~Е~", "~Ж~", "~З~", "~И~", "~К~", "Л"};
    }


    public View(Cell[][] dataToView) {
        this.dataToView = dataToView;
        final JFrame frame = new JFrame("Морской бой 06");
        frame.setLayout(new BorderLayout());
        frame.setSize(900, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(10, 10));
        panel.setSize(200, 200);
        panel.setBackground(Color.lightGray);
        panel.setVisible(true);

        JLabel[][] data = new JLabel[ConfigOfGame.getMe().getHeight() + 1][ConfigOfGame.getMe().getWidth() + 1];
        for (int y = 1; y <= ConfigOfGame.getMe().getHeight(); y++) {
            for (int x = 1; x <= ConfigOfGame.getMe().getWidth(); x++) {
                data[x][y] = new JLabel();
                data[x][y].setText("H");
                data[x][y].setBackground(Color.red);
                data[x][y].setVisible(true);

                panel.add(data[x][y]);
            }

        }



        frame.add(panel, BorderLayout.CENTER);

        frame.setVisible(true);

    }


}
