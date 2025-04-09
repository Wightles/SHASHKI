package ru.nstu.rgz.model;

import java.util.LinkedList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Pawn {
    public static final int NUM_COLS = 8; // Количество колонок на доске
    public static final int NUM_ROWS = 8; // Количество строк на доске
    public static final Image WHITE_PAWN_SPRITE = loadImage(Color.WHITE, false); // Изображение белой шашки
    public static final Image BLACK_PAWN_SPRITE = loadImage(Color.BLACK, false); // Изображение черной шашки
    private static final String assetsFolder = "/"; // Путь к папке с изображениями
    public static Pawn[][] pawnsPosition; // Матрица позиций шашек на доске
    static {
        initBoard(); // Инициализация доски
    }
    private String name; // Имя шашки
    protected Color color; // Цвет шашки
    protected int row; // Текущая строка шашки
    protected int col; // Текущая колонка шашки

    // Конструктор шашки
    public Pawn(Color color, int rowInit, int colInit) {
        this.color = color;
        this.row = rowInit;
        this.col = colInit;
    }

    // Получить цвет шашки
    public Color getColor() {
        return this.color;
    }

    // Получить имя шашки
    public String getName() {
        return this.name;
    }

    // Получить текущую строку шашки
    public int getRow() {
        return this.row;
    }

    // Получить текущую колонку шашки
    public int getCol() {
        return this.col;
    }

    // Переместить шашку на новую позицию
    public void moveTo(int newRow, int newCol) {
        // Если произошел захват, это будет выполнено
        if (Math.abs(newRow - this.row) > 1 && Math.abs(newCol - this.col) > 1) {
            System.out.println("Атакованы следующие пешки: " + this.findAllCapturedPawns(newRow, newCol));
            this.findAllCapturedPawns(newRow, newCol).forEach(pawn -> {
                pawn.getCaptured();
            });
        }

        pawnsPosition[this.row][this.col] = null; // Очистить старую позицию
        pawnsPosition[newRow][newCol] = this; // Установить новую позицию
        this.row = newRow; // Обновить строку
        this.col = newCol; // Обновить колонку

        // Продвижение шашки в дамки
        if (canBePromoted()) {
            this.getPromoted();
        }
    }

    // Проверить, можно ли переместиться на новую позицию
    public boolean canMoveTo(int newRow, int newCol) {
        return pawnsPosition[newRow][newCol] == null;
    }

    // Захват шашки
    public void getCaptured() {
        Pawn capturedPawn = Pawn.pawnsPosition[this.row][this.col];
        Player hostilePlayer = Player.getPlayerWithColor(this.getColor().equals(Color.WHITE) ? Color.BLACK : Color.WHITE);
        hostilePlayer.addCapturedPawn(capturedPawn);
        Pawn.pawnsPosition[this.row][this.col] = null;
    }

    // Получить список следующих возможных ходов
    public List<Cell> getNextCells() {
        return scanNeighbors();
    }

    // Сканировать соседние клетки для поиска возможных ходов
    protected List<Cell> scanNeighbors() {
        List<Cell> nextCells = new LinkedList<>();
        int[] closeLocationsRow = new int[] {-1, 1, 1, -1};
        int[] closeLocationsCol = new int[] {-1, -1, 1, 1};
        boolean hasToCaptureAPawn = false;

        for (int i = 0; i < 4; i++) {
            int neighborRow = this.row + closeLocationsRow[i];
            int neighborCol = this.col + closeLocationsCol[i];

            // Проверка, находится ли клетка в пределах доски
            if (Pawn.isOffRange(neighborRow, neighborCol))
                continue;

            Pawn neighbor = pawnsPosition[neighborRow][neighborCol];

            // Пустая клетка и вражеский сосед
            if (neighbor == null) {
                if (hasToCaptureAPawn)
                    continue;

                if (this.color == Color.WHITE && neighborRow < this.row) {
                    nextCells.add(new Cell(neighborRow, neighborCol));
                } else if (this.color == Color.BLACK && neighborRow > this.row) {
                    nextCells.add(new Cell(neighborRow, neighborCol));
                }
            } else if (neighbor.getColor() != this.getColor()) {
                if (neighbor.canBeCapturedFrom(this.row, this.col)) {
                    if (!hasToCaptureAPawn)
                        nextCells.clear();
                    nextCells.add(new Cell(neighborRow + closeLocationsRow[i], neighborCol + closeLocationsCol[i]));
                    hasToCaptureAPawn = true;
                }
            }
        }
        return nextCells;
    }

    // Вычислить коэффициент расстояния
    protected int calculateDistanceCoefficient(int hostileRow, int hostileCol) {
        int distanceCoefficient = 0;

        int rowAfterCapture = this.row;
        int colAfterCapture = this.col;
        Pawn cellAfterCapture;

        do {
            rowAfterCapture = hostileRow < this.row ? rowAfterCapture + 1 : rowAfterCapture - 1;
            colAfterCapture = hostileCol < this.col ? colAfterCapture + 1 : colAfterCapture - 1;

            if (isOffRange(rowAfterCapture, colAfterCapture)) {
                return 0;
            }

            cellAfterCapture = pawnsPosition[rowAfterCapture][colAfterCapture];

            if (cellAfterCapture != null && !cellAfterCapture.getColor().equals(this.color)) {
                return 0;
            }
            distanceCoefficient++;
        } while (cellAfterCapture != null);
        return distanceCoefficient;
    }

    // Проверить, может ли шашка быть захвачена с указанной позиции
    public boolean canBeCapturedFrom(int hostileRow, int hostileCol) {
        int rowAfterCapture = hostileRow < this.row ? this.row + 1 : this.row - 1;
        int colAfterCapture = hostileCol < this.col ? this.col + 1 : this.col - 1;

        if (isOffRange(rowAfterCapture, colAfterCapture)) {
            return false;
        }

        Pawn cellAfterCapture = pawnsPosition[rowAfterCapture][colAfterCapture];
        return cellAfterCapture == null;
    }

    // Найти все захваченные шашки между текущей и новой позицией
    public List<Pawn> findAllCapturedPawns(int newRow, int newCol) {
        List<Pawn> capturedPawns = new LinkedList<>();

        int inspectedRow = this.row < newRow ? this.row + 1 : this.row - 1;
        int inspectedCol = this.col < newCol ? this.col + 1 : this.col - 1;
        while (inspectedRow != newRow && inspectedCol != newCol) {
            Pawn inspectedPawn = Pawn.pawnsPosition[inspectedRow][inspectedCol];

            // Тестовый случай только
            if (inspectedPawn == null) {
                inspectedRow = inspectedRow < newRow ? inspectedRow + 1 : inspectedRow - 1;
                inspectedCol = inspectedCol < newCol ? inspectedCol + 1 : inspectedCol - 1;
                if (isOffRange(inspectedRow, inspectedCol)) {
                    break;
                } else {
                    continue;
                }
            }

            if (inspectedPawn.getColor() != this.getColor()) {
                capturedPawns.add(inspectedPawn);
            }
            inspectedRow = inspectedRow < newRow ? inspectedRow + 1 : inspectedRow - 1;
            inspectedCol = inspectedCol < newCol ? inspectedCol + 1 : inspectedCol - 1;
        }
        return capturedPawns;
    }

    // Проверить, может ли шашка быть продвинута в дамки
    public boolean canBePromoted() {
        if (this.row == NUM_ROWS - 1 && this.color.equals(Color.BLACK)) {
            return true;
        } else if (this.row == 0 && this.color.equals(Color.WHITE)) {
            return true;
        }
        return false;
    }

    // Продвинуть шашку в дамки
    public void getPromoted() {
        Pawn.pawnsPosition[this.row][this.col] = new King(this.color, this.row, this.col);
    }

    // Получить изображение шашки в зависимости от цвета
    public Image getSprite(Color wantedColor) {
        return wantedColor.equals(Color.WHITE) ? Pawn.WHITE_PAWN_SPRITE : Pawn.BLACK_PAWN_SPRITE;
    }

    // Загрузить изображение шашки
    protected static Image loadImage(Color wantedColor, boolean isKing) {
        String imgColor = wantedColor.equals(Color.WHITE) ? "white" : "black";
        String imgPath = isKing ? assetsFolder + imgColor + "-king.png" : assetsFolder + imgColor + "-pawn.png";
        return new Image(Pawn.class.getResource(imgPath).toExternalForm());
    }

    // Проверить, находится ли позиция вне диапазона доски
    public static boolean isOffRange(int row, int col) {
        return row < 0 || row >= NUM_ROWS || col < 0 || col >= NUM_COLS;
    }

    // Инициализация доски и расстановка шашек
    public static void initBoard() {
        pawnsPosition = new Pawn[NUM_ROWS][NUM_COLS];
        for (int i = 0; i < 3; i++) { // Устанавливаем три ряда для черных шашек
            for (int j = 0; j < NUM_COLS; j++) {
                if ((i + j) % 2 != 0) {
                    pawnsPosition[i][j] = new Pawn(Color.BLACK, i, j);
                }
            }
        }
        for (int i = NUM_ROWS - 3; i < NUM_ROWS; i++) { // Устанавливаем три ряда для белых шашек
            for (int j = 0; j < NUM_COLS; j++) {
                if ((i + j) % 2 != 0) {
                    pawnsPosition[i][j] = new Pawn(Color.WHITE, i, j);
                }
            }
        }
    }

    // Переопределение метода toString() для вывода информации о шашке
    @Override
    public String toString() {
        String colorStr;
        if (this.color.equals(Color.WHITE)) {
            colorStr = "Белый";
        } else {
            colorStr = "Черный";
        }
        return colorStr + " [ряд=" + this.row + ", столбец=" + this.col + "]";
    }
}
