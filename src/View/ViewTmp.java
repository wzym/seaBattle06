package View;

import Model.Cell;
import Model.Game;

public class ViewTmp {
    private Cell[][] dataToView;
    private String[] numberToLetter;

    {
        this.numberToLetter = new String[]      // массив для вывода координаты "x" в буквенном кириллическом виде
                {"~А~", "~Б~", "~В~", "~Г~", "~Д~", "~Е~", "~Ж~", "~З~", "~И~", "~К~", "~Л~", "~М~", "~Н~", "~О~",
                        "~П~", "~Р~", "~С~", "~Т~", "~У~", "~Ф~", "~Х~", "~Ц~", "~Ч~", "~Ш~", "~Ы~", "~Э~", "~Ю~", "~Я~"};
    }

    public ViewTmp(Cell[][] dataToView) {
        this.dataToView = dataToView;
        this.viewField();
    }

    private void viewField() {           // выводит поле на экран
        for (int i = 0; i < Game.WIDTH; i++) {         // Выводим строчку с буквами в соответствии с шириной поля
            System.out.print(this.numberToLetter[i]);
        }
        System.out.println();
        for (int y = 1; y < Game.HEIGHT; y++) {         // считаем с единицы из-за места для буфера кораблей; к тому же, удобней
            for (int x = 1; x < Game.WIDTH; x++) {
                switch (this.dataToView[x][y].getStatus()) {
                    case WATER:
                        System.out.print("~~~");
                        break;
                    case DECK:
                        System.out.print("~H~");
                        break;
                    case BUFFER:
                        System.out.print("~B~");
                        break;
                    case DAMAGED_SHIP:
                        System.out.print("~+~");
                        break;
                    case DAMAGED_DECK:
                        System.out.print("~*~");
                        break;
                    case DAMAGED_WATER:
                        System.out.print("~V~");
                        break;
                }
            }
            System.out.println(y);
        }
    }
}
