package ru.nstu.rgz.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import ru.nstu.rgz.model.Cell;
import ru.nstu.rgz.model.Game;
import ru.nstu.rgz.model.Pawn;
import ru.nstu.rgz.model.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.control.ButtonBar.ButtonData;

public class GameController {
    private static boolean canHighlightCells = true; // Переменная для отслеживания возможности подсветки клеток
    private static Pawn currentlySelectedPawn = null; // Текущая выбранная пешка
    private static Game currentGame = new Game(); // Текущая игра
    private static final ButtonType RESTART = new ButtonType("Перезагрузка", ButtonData.APPLY);
    private static final ButtonType QUIT = new ButtonType("Выход", ButtonData.APPLY);

    // Обработка нажатия кнопки начала игры
    public void handleStartGameButton(GridPane board, Stage primaryStage) {
        if (currentGame.hasStarted()) {
            restartGame(board, primaryStage);
        }
        currentGame.setHasStarted(true);
        currentGame.newTour();
    }

    // Свойство количества туров
    public IntegerProperty numberOfTours() {
        return currentGame.numberOfTours();
    }

    // Свойство текущего игрока
    public StringProperty whoIsPlayingProperty() {
        return currentGame.whoIsPlayingProperty();
    }

    // Свойство текущей шашки
    public ObjectProperty<Image> currentPawnProperty() {
        return currentGame.currentPawnProperty();
    }

    // Обновление ссылок на клетки
    public void updateCellReferential(int i, int j) {
        Pawn pawn = Pawn.pawnsPosition[i][j];
        if (pawn != null) {
            if (!pawn.getColor().equals(currentGame.getWhoIsPlaying().getPlayingColor()))
                return;

            currentlySelectedPawn = pawn;
            System.out.println("Обычный ход шашкой !!");
            List<Cell> cellsToHighlight = pawn.getNextCells();
            cellsToHighlight.forEach(cell -> Cell.highlightedCells[cell.getRow()][cell.getCol()] = true);
        }
    }

    // Показ подсвеченных клеток
    public void showHighlightedCells(GridPane board, Stage primaryStage) {
        if (!canHighlightCells || allFalse(Cell.highlightedCells)) {
            return;
        }
        canHighlightCells = false;
        for (int i = 0; i < Pawn.NUM_ROWS; i++) {
            for (int j = 0; j < Pawn.NUM_COLS; j++) {
                if (Cell.highlightedCells[i][j]) {
                    StackPane cell = new StackPane();
                    cell.setStyle("-fx-background-color: GREEN");
                    final AtomicInteger row = new AtomicInteger(i);
                    final AtomicInteger col = new AtomicInteger(j);
                    cell.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                        currentlySelectedPawn.moveTo(row.get(), col.get());
                        renderBoard(board, false, primaryStage);
                        currentlySelectedPawn = null;
                        canHighlightCells = true;
                        Player winner = Game.getWinner();
                        if (winner == null) {
                            currentGame.newTour();
                        } else {
                            showWinnerDialog(winner, primaryStage, board);
                        }
                    });
                    board.add(cell, j, i);
                    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
                        board.getChildren().remove(cell);
                        canHighlightCells = true;
                        Cell.darkenAllCells();
                    }));
                    timeline.play();
                }
            }
        }
        Cell.darkenAllCells();
    }

    // Отрисовка доски
    public void renderBoard(GridPane board, boolean firstInit, Stage primaryStage) {
        for (int i = 0; i < Pawn.NUM_ROWS; i++) {
            for (int j = 0; j < Pawn.NUM_COLS; j++) {
                StackPane cell = new StackPane();
                final AtomicInteger row = new AtomicInteger(i);
                final AtomicInteger col = new AtomicInteger(j);
                cell.addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
                    if (!currentGame.hasStarted())
                        return;
                    updateCellReferential(row.get(), col.get());
                    showHighlightedCells(board, primaryStage);
                });
                Pawn pawn = Pawn.pawnsPosition[i][j];
                if (pawn != null) {
                    ImageView pawnView = new ImageView(pawn.getSprite(pawn.getColor()));
                    pawnView.setFitWidth(55);
                    pawnView.setPreserveRatio(true);
                    pawnView.setSmooth(true);
                    pawnView.setCache(true);
                    cell.getChildren().add(pawnView);
                }
                if ((i + j) % 2 == 0) {
                    cell.setStyle("-fx-background-color: #A9A9A9"); // Светло-серый
                } else {
                    cell.setStyle("-fx-background-color: #2F4F4F"); // Темно-серый
                }
                Node nodeToModify = getNodeByCoordinate(j, i, board);
                if (nodeToModify == null) {
                    board.add(cell, j, i);
                } else if (nodeToModify instanceof StackPane) {
                    if (!firstInit)
                        board.getChildren().remove(nodeToModify);
                    board.add(cell, j, i);
                }
            }
        }
    }

    // Получение узла по координатам
    Node getNodeByCoordinate(Integer row, Integer column, GridPane board) {
        for (Node node : board.getChildren()) {
            if (GridPane.getColumnIndex(node) == row && GridPane.getRowIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

    // Проверка, все ли значения массива равны false
    boolean allFalse(boolean[][] array) {
        for (boolean[] subArray : array) {
            for (boolean b : subArray) {
                if (b) {
                    return false;
                }
            }
        }
        return true;
    }

    // Получение списка захваченных пешек
    public ObservableList<Pawn> getCapturedSprite(Color color) {
        return Player.getCapturedPawn(color);
    }

    // Показ диалога с победителем
    public void showWinnerDialog(Player player, Stage primaryStage, GridPane board) {
        Alert alert = new Alert(Alert.AlertType.NONE, player.getName() + " Выйграл !", RESTART, QUIT);
        alert.showAndWait();
        ButtonType result = alert.getResult();
        if (result == QUIT) {
            primaryStage.close();
        } else {
            restartGame(board, primaryStage);
        }
    }

    // Перезапуск игры
    public void restartGame(GridPane board, Stage primaryStage) {
        Pawn.initBoard();
        renderBoard(board, false, primaryStage);
        currentGame.restartGame();
    }
}
