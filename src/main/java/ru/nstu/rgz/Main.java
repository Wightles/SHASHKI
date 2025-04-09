package ru.nstu.rgz;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import ru.nstu.rgz.view.HomeView;
import ru.nstu.rgz.view.GameView;

import java.io.File;

public class Main extends Application {
    private static MediaPlayer mediaPlayer;
    public static boolean isMusicPlaying = false; // Изменено на false, чтобы музыка не играла при запуске

    public static final int WIN_WIDTH_GAME = 1080;
    public static final int WIN_HEIGHT_GAME = 720;
    public static final int WIN_WIDTH_ACCUEIL = 300;
    public static final int WIN_HEIGHT_ACCUEIL = 270;

    private Stage primaryStage;
    private Scene gameScene;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        try {
            BorderPane gameRoot = new BorderPane();
            GameView.initView(gameRoot, primaryStage);
            gameScene = new Scene(gameRoot, WIN_WIDTH_GAME, WIN_HEIGHT_GAME);

            StackPane gameSettingsRoot = new StackPane();
            HomeView.initView(gameSettingsRoot, primaryStage, gameScene);
            Scene gameSettingsScene = new Scene(gameSettingsRoot, WIN_WIDTH_ACCUEIL, WIN_HEIGHT_ACCUEIL);

            primaryStage.setScene(gameSettingsScene);
            primaryStage.setResizable(true);
            primaryStage.setOnShown(event -> primaryStage.centerOnScreen());
            primaryStage.show();

            Image icon = new Image(getClass().getResource("/sashki.png").toExternalForm());
            primaryStage.getIcons().add(icon);
            primaryStage.setTitle("Шашки");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Инициализация музыки с выбранным файлом
    public static void initMusic(File musicFile) {
        if (musicFile != null) {
            Media sound = new Media(musicFile.toURI().toString());
            mediaPlayer = new MediaPlayer(sound);
        }
    }

    public static MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    // Метод для включения/выключения музыки
    public static void toggleMusic() {
        if (mediaPlayer != null) {
            if (isMusicPlaying) {
                mediaPlayer.pause();
            } else {
                mediaPlayer.play();
            }
            isMusicPlaying = !isMusicPlaying;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
