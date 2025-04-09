package ru.nstu.rgz.view;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import ru.nstu.rgz.Main;
import ru.nstu.rgz.controller.GameController;
import ru.nstu.rgz.model.Pawn;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class GameView {
    private static GameController gameController = new GameController();
    private static Button musicButton;
    private static MediaPlayer mediaPlayer;
    private static File musicFile;

    public static void initView(BorderPane root, Stage primaryStage) {
        // Инициализация медиа проигрывателя
        Main mainInstance = new Main();
        mainInstance.initMusic(musicFile);

        GameView.mediaPlayer = Main.getMediaPlayer(); // Получаем экземпляр mediaPlayer

        // Центр - создание и инициализация доски
        GridPane board = new GridPane();
        buildGrid(board);
        initGrid(board, primaryStage);
        root.setCenter(board);

        // Лево - создание панели информации о текущем ходе
        ImageView pawnView = new ImageView();
        pawnView.imageProperty().bind(gameController.currentPawnProperty());
        pawnView.setFitWidth(45);
        pawnView.setPreserveRatio(true);
        pawnView.setSmooth(true);
        pawnView.setCache(true);

        Label currentPlayerLabel = new Label();
        currentPlayerLabel.textProperty().bind(gameController.whoIsPlayingProperty());
        currentPlayerLabel.setPrefWidth(100);
        currentPlayerLabel.setPadding(new Insets(0, 0, 0, 5));

        Label infoTour = new Label("Шаг ");
        infoTour.setStyle("-fx-font-weight: bold;");
        Label currentTour = new Label();
        currentTour.textProperty().bind(gameController.numberOfTours().asString());
        HBox tourPanel = new HBox(infoTour, currentTour);
        tourPanel.setAlignment(Pos.CENTER);

        VBox infoPanel = new VBox(pawnView, currentPlayerLabel, tourPanel);
        infoPanel.setPadding(new Insets(50, 50, 50, 50));
        infoPanel.setAlignment(Pos.CENTER);
        infoPanel.setSpacing(20);
        root.setLeft(infoPanel);

        // Право - создание панели управления игрой
        Label gameControls = new Label("Управление игрой");
        Button startGameButton = new Button("Начать игру");
        startGameButton.setOnAction(event -> {
            gameController.handleStartGameButton(board, primaryStage);
            if (startGameButton.getText().equals("Начать игру")) {
                startGameButton.setText("Перезапустить игру");
            }
        });

        musicButton = new Button("Включить музыку");
        musicButton.setOnAction(event -> toggleMusic());

        Button chooseMusicButton = new Button("Выбрать музыку");
        chooseMusicButton.setOnAction(event -> chooseMusicFile(primaryStage));

        Button quitGameButton = new Button("Выйти");
        quitGameButton.setOnAction(event -> {
            ButtonType yesButton = new ButtonType("Да", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("Нет", ButtonBar.ButtonData.NO);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Весь прогресс будет потерян. Вы уверены?", yesButton, noButton);
            alert.setTitle("Подтверждение выхода");
            alert.setHeaderText(null);
            alert.showAndWait();

            ButtonType result = alert.getResult();
            if (result == yesButton) {
                primaryStage.close();
            }
        });

        VBox btnPanel = new VBox(gameControls, startGameButton, musicButton, chooseMusicButton, quitGameButton);
        btnPanel.setAlignment(Pos.CENTER);
        btnPanel.setSpacing(20);
        btnPanel.setPadding(new Insets(50, 50, 50, 50));
        root.setRight(btnPanel);

        // Низ - создание панели захваченных шашек
        HBox capturedPawnsHBox = new HBox();
        ObservableList<Pawn> capturedPawnW = gameController.getCapturedSprite(Color.WHITE);
        capturedPawnW.addListener(new ListChangeListener<Pawn>() {

            @Override
            public void onChanged(Change<? extends Pawn> arg0) {

                if (capturedPawnW.isEmpty()) {
                    capturedPawnsHBox.getChildren().clear();
                    return;
                }
                ImageView pawnView = new ImageView(Pawn.BLACK_PAWN_SPRITE);
                pawnView.setFitWidth(55);
                pawnView.setPreserveRatio(true);
                pawnView.setSmooth(true);
                pawnView.setCache(true);
                capturedPawnsHBox.getChildren().add(pawnView);
            }

        });
        capturedPawnsHBox.setAlignment(Pos.CENTER);
        capturedPawnsHBox.setPadding(new Insets(0, 0, 10, 0));
        root.setBottom(capturedPawnsHBox);

        // Верх - создание панели захваченных шашек для другого цвета
        HBox capturedPawnsHBoxTop = new HBox();
        ObservableList<Pawn> capturedPawn = gameController.getCapturedSprite(Color.BLACK);
        capturedPawn.addListener(new ListChangeListener<Pawn>() {

            @Override
            public void onChanged(Change<? extends Pawn> arg0) {
                if (capturedPawn.isEmpty()) {
                    capturedPawnsHBoxTop.getChildren().clear();
                    return;
                }
                ImageView pawnView = new ImageView(Pawn.WHITE_PAWN_SPRITE);
                pawnView.setFitWidth(55);
                pawnView.setPreserveRatio(true);
                pawnView.setSmooth(true);
                pawnView.setCache(true);
                capturedPawnsHBoxTop.getChildren().add(pawnView);

            }

        });
        capturedPawnsHBoxTop.setAlignment(Pos.CENTER);
        root.setTop(capturedPawnsHBoxTop);
    }

    // Метод для выбора музыкального файла
    private static void chooseMusicFile(Stage primaryStage) {
        if (Main.isMusicPlaying) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Вы не можете выбрать другой музыкальный файл, пока музыка играет. Сначала выключите музыку.");
            alert.showAndWait();
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("MP3 files", "*.mp3"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                musicFile = selectedFile;
                Main.initMusic(musicFile);
                musicButton.setText("Включить музыку");
                Main.isMusicPlaying = false;
            }
        }
    }

    // Метод для построения сетки доски
    public static void buildGrid(GridPane board) {
        board.setGridLinesVisible(false);

        for (int i = 0; i < Pawn.NUM_COLS; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100 / Pawn.NUM_COLS);
            board.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < Pawn.NUM_COLS; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100 / Pawn.NUM_COLS);
            board.getRowConstraints().add(rowConst);
        }
        board.setMaxSize(600, 600);
    }

    // Метод для инициализации сетки доски
    public static void initGrid(GridPane board, Stage primaryStage) {
        gameController.renderBoard(board, true, primaryStage);
    }

    // Метод для отображения подсвеченных клеток
    public void showHighlightedCells(GridPane board, Stage primaryStage) {
        gameController.showHighlightedCells(board, primaryStage);
    }

    // Метод для включения или выключения музыки

    private static void toggleMusic() {
        if (musicFile == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText(null);
            alert.setContentText("Сначала выберите музыкальный файл.");
            alert.showAndWait();
        } else {
            Main.toggleMusic();
            if (Main.isMusicPlaying) {
                musicButton.setText("Выключить музыку");
            } else {
                musicButton.setText("Включить музыку");
            }
        }
    }
}

