package ru.nstu.rgz.view;

import ru.nstu.rgz.controller.HomeController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import ru.nstu.rgz.model.Pawn;

public class HomeView {
    private static HomeController homeController = new HomeController();

    public static void initView(StackPane gameSettingsRoot, Stage primaryStage, Scene gameScene) {
        // Установка стиля фона для корневого элемента настроек игры
        gameSettingsRoot.setStyle("-fx-background-color: white;");

        // Создание кнопки "Играть"
        Button button1 = new Button("ИГРАТЬ !");

        // Создание вертикального контейнера для элементов
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(20, 0, 0, 0));
        vbox.setAlignment(Pos.TOP_CENTER);

        // Заголовок
        Label label = new Label("ШАШКИ");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        label.setStyle("-fx-text-fill: #4CAF50;");
        vbox.getChildren().add(label);
        vbox.getChildren().add(new Label(" "));

        // Информация для Игрока 1
        Label labelPlayer1 = new Label("Игрок 1: ");
        TextField tfPlayer1 = new TextField();
        labelPlayer1.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        // Изображение шашки для Игрока 1
        ImageView imageView1 = new ImageView(Pawn.BLACK_PAWN_SPRITE);
        imageView1.setFitHeight(20);
        imageView1.setFitWidth(20);
        HBox hboxPlayer1 = new HBox(labelPlayer1, tfPlayer1, imageView1);
        hboxPlayer1.setSpacing(20);
        hboxPlayer1.setAlignment(Pos.CENTER);
        vbox.getChildren().add(hboxPlayer1);

        // Информация для Игрока 2
        Label labelPlayer2 = new Label("Игрок 2: ");
        TextField tfPlayer2 = new TextField();
        labelPlayer2.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        // Изображение шашки для Игрока 2
        ImageView imageView2 = new ImageView(Pawn.WHITE_PAWN_SPRITE);
        imageView2.setFitHeight(20);
        imageView2.setFitWidth(20);
        HBox hboxPlayer2 = new HBox(labelPlayer2, tfPlayer2, imageView2);
        hboxPlayer2.setSpacing(20);
        hboxPlayer2.setAlignment(Pos.CENTER);
        vbox.getChildren().add(hboxPlayer2);
        vbox.getChildren().add(new Label(" "));

        // Добавление кнопки "Играть" в контейнер
        vbox.getChildren().add(button1);

        // Добавление вертикального контейнера в корневой элемент
        gameSettingsRoot.getChildren().addAll(vbox);

        // Стиль кнопки "Играть"
        button1.setStyle("-fx-background-color: transparent; -fx-text-fill: #4CAF50; -fx-border-color: #4CAF50; -fx-border-width: 0 0 2 0;");
        button1.setPrefWidth(150);
        button1.setPrefHeight(50);
        button1.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        // Обработка нажатия на кнопку "Играть"
        button1.setOnAction(e -> {
            String player1Name = tfPlayer1.getText();
            String player2Name = tfPlayer2.getText();

            // Проверка, что оба поля ввода не пустые
            if (player1Name.isEmpty() || player2Name.isEmpty()) {
                homeController.isEmpty();
            } else if (player1Name.equals(player2Name)) {
                // Проверка, что ники не совпадают
                homeController.showDuplicateNameError();
            } else {
                // Инициализация имен игроков и переход к сцене игры
                homeController.initNamePlayer(player1Name, player2Name);
                primaryStage.setScene(gameScene);
                primaryStage.centerOnScreen();  // Центрирование окна игры на экране
            }
        });
    }
}
