package ru.nstu.rgz.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

public class Player {
    public static List<Player> players; // Список всех игроков
    public final static int NUMBER_OF_STARTING_PAWNS = 24; // Количество шашек в начале игры
    private String name; // Имя игрока
    private Color playingColor; // Цвет шашек игрока
    private int numberOfRemainingPawns; // Количество оставшихся шашек
    private boolean isPlaying; // Флаг, указывающий, что игрок сейчас ходит
    private static final ObservableList<Pawn> CAPTURED_PAWNS_FOR_WHITE = FXCollections.observableArrayList(); // Список захваченных белых шашек
    private static final ObservableList<Pawn> CAPTURED_PAWNS_FOR_BLACK = FXCollections.observableArrayList(); // Список захваченных черных шашек

    // Конструктор игрока
    public Player(String name, Color color) {
        this.name = name;
        this.playingColor = color;
        this.numberOfRemainingPawns = NUMBER_OF_STARTING_PAWNS;
        this.isPlaying = false;
    }

    // Получить имя игрока
    public String getName() {
        return this.name;
    }

    // Получить цвет шашек игрока
    public Color getPlayingColor() {
        return this.playingColor;
    }

    // Получить количество оставшихся шашек
    public int getNumberOfRemainingPawns() {
        return this.numberOfRemainingPawns;
    }

    // Проверить, ходит ли сейчас игрок
    public boolean isPlaying() {
        return this.isPlaying;
    }

    // Уменьшить количество оставшихся шашек на одну
    public void declarePawnLoss() {
        this.numberOfRemainingPawns--;
    }

    // Начать ход
    public void playTour() {
        this.isPlaying = true;
    }

    // Закончить ход
    public void endTour() {
        this.isPlaying = false;
    }

    // Добавить захваченную шашку в соответствующий список
    public void addCapturedPawn(Pawn capturedPawn) {
        if (capturedPawn.getColor().equals(Color.WHITE)) {
            CAPTURED_PAWNS_FOR_BLACK.add(capturedPawn);
        } else {
            CAPTURED_PAWNS_FOR_WHITE.add(capturedPawn);
        }
    }

    // Получить список захваченных шашек определенного цвета
    public static ObservableList<Pawn> getCapturedPawn(Color color) {
        return Color.WHITE.equals(color) ? CAPTURED_PAWNS_FOR_WHITE : CAPTURED_PAWNS_FOR_BLACK;
    }

    // Получить количество захваченных шашек определенного цвета
    public static int getNumberOfCapturedPawns(Color color) {
        return Color.WHITE.equals(color) ? CAPTURED_PAWNS_FOR_WHITE.size() : CAPTURED_PAWNS_FOR_BLACK.size();
    }

    // Инициализация игроков
    public static void initPlayers(String name1, String name2) {
        players = new ArrayList<>();
        Player p1 = new Player(name1, Color.BLACK);
        Player p2 = new Player(name2, Color.WHITE);
        players.add(p1);
        players.add(p2);
    }

    // Сброс состояния игроков
    public static void resetPlayers() {
        CAPTURED_PAWNS_FOR_BLACK.clear();
        CAPTURED_PAWNS_FOR_WHITE.clear();
    }

    // Получить игрока по цвету его шашек
    public static Player getPlayerWithColor(Color color) {
        return players.stream().filter(player -> player.getPlayingColor() == color).collect(Collectors.toList()).get(0);
    }
}
