package ru.nstu.rgz.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Game {
    private boolean hasStarted; // Флаг начала игры
    private IntegerProperty numberOfPlayedTour; // Количество сыгранных туров
    private StringProperty whoIsPlayingProperty; // Свойство, содержащее имя текущего игрока
    private ObjectProperty<Image> currentPawnProperty; // Свойство, содержащее изображение текущей пешки
    private Player whoIsPlaying; // Текущий игрок

    // Конструктор
    public Game() {
        this.hasStarted = false;
        this.numberOfPlayedTour = new SimpleIntegerProperty(0);
        this.whoIsPlayingProperty = new SimpleStringProperty("");
        this.currentPawnProperty = new SimpleObjectProperty<>();
        this.whoIsPlaying = null;
        if (Player.players != null)
            Player.resetPlayers(); // Сброс игроков, если они уже существуют
    }

    // Метод проверки начала игры
    public boolean hasStarted() {
        return this.hasStarted;
    }

    // Метод установки флага начала игры
    public void setHasStarted(boolean newValue) {
        this.hasStarted = newValue;
    }

    // Метод получения текущего игрока
    public Player getWhoIsPlaying() {
        return this.whoIsPlaying;
    }

    // Метод получения свойства текущего игрока
    public StringProperty whoIsPlayingProperty() {
        return whoIsPlayingProperty;
    }

    // Метод получения значения свойства текущего игрока
    public String getWhoIsPlayingPropertyValue() {
        return whoIsPlayingProperty.get();
    }

    // Метод установки значения свойства текущего игрока
    public void setWhoIsPlaying(String value) {
        whoIsPlayingProperty.set(value);
    }

    // Метод получения свойства количества туров
    public IntegerProperty numberOfTours() {
        return numberOfPlayedTour;
    }

    // Метод получения значения количества сыгранных туров
    public int getNumberOfPlayedTour() {
        return numberOfPlayedTour.get();
    }

    // Метод увеличения количества сыгранных туров
    public void incrementNumberOfPlayedTour() {
        numberOfPlayedTour.set(getNumberOfPlayedTour() + 1);
    }

    // Метод получения свойства текущей пешки
    public ObjectProperty<Image> currentPawnProperty() {
        return this.currentPawnProperty;
    }

    // Метод получения значения свойства текущей пешки
    public Image getCurrentPawnPropertyValue() {
        return this.currentPawnProperty.get();
    }

    // Метод установки значения свойства текущей пешки
    public void setCurrentPawnPropertyValue(Image newImage) {
        currentPawnProperty.set(newImage);
    }

    // Метод перезапуска игры
    public void restartGame() {
        this.hasStarted = false;
        this.numberOfPlayedTour.set(0);
        this.whoIsPlayingProperty.set("");
        this.whoIsPlaying = null;
        if (Player.players != null)
            Player.resetPlayers(); // Сброс игроков
    }

    // Метод начала нового тура
    public void newTour() {
        this.incrementNumberOfPlayedTour();
        if (this.whoIsPlaying == null) {
            this.whoIsPlaying = Player.getPlayerWithColor(Color.WHITE);
            this.whoIsPlaying.playTour();
            this.setCurrentPawnPropertyValue(Pawn.WHITE_PAWN_SPRITE);
        } else {
            Player previousPlayer = this.whoIsPlaying;
            Player nextPlayer;

            if (this.whoIsPlaying.getPlayingColor().equals(Color.WHITE)) {
                nextPlayer = Player.getPlayerWithColor(Color.BLACK);
                this.setCurrentPawnPropertyValue(Pawn.BLACK_PAWN_SPRITE);
            } else {
                nextPlayer = Player.getPlayerWithColor(Color.WHITE);
                this.setCurrentPawnPropertyValue(Pawn.WHITE_PAWN_SPRITE);
            }
            this.whoIsPlaying = nextPlayer;
            previousPlayer.endTour();
            nextPlayer.playTour();
        }
        setWhoIsPlaying(this.whoIsPlaying.getName() + " ходит!"); // Установка сообщения о текущем ходе
    }

    // Метод получения победителя
    public static Player getWinner() {
        if (Player.getNumberOfCapturedPawns(Color.WHITE) == 12) {
            return Player.getPlayerWithColor(Color.WHITE);
        } else if (Player.getNumberOfCapturedPawns(Color.BLACK) == 12) {
            return Player.getPlayerWithColor(Color.BLACK);
        } else {
            return null; // Если победителя нет
        }
    }
}
