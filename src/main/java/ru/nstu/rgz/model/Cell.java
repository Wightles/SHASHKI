package ru.nstu.rgz.model;

import java.util.Arrays;

public class Cell {
    private int row; // Ряд клетки
    private int col; // Столбец клетки
    public static boolean[][] highlightedCells = new boolean[Pawn.NUM_ROWS][Pawn.NUM_COLS]; // Массив подсвеченных клеток

    // Конструктор клетки
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    // Метод для затемнения всех клеток (снятия подсветки)
    public static void darkenAllCells() {
        Arrays.stream(highlightedCells).forEach(row -> Arrays.fill(row, false));
    }

    // Метод получения ряда клетки
    public int getRow() {
        return row;
    }

    // Метод получения столбца клетки
    public int getCol() {
        return col;
    }

    // Переопределение метода equals для сравнения клеток
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Cell)) {
            return false;
        }

        Cell c = (Cell) o;
        return Integer.compare(this.row, c.row) == 0 && Integer.compare(this.col, c.col) == 0; // Сравнение рядов и столбцов
    }

    // Переопределение метода toString для представления клетки в виде строки
    @Override
    public String toString() {
        return "Клетка: [ряд=" + this.row + ", столбец=" + this.col + "]";
    }
}
