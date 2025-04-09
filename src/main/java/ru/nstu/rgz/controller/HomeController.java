package ru.nstu.rgz.controller;

import ru.nstu.rgz.model.Player;
import javafx.scene.control.Alert;

public class HomeController {

    // Инициализация имен игроков
    public void initNamePlayer(String name1, String name2) {
        Player.initPlayers(name1, name2);
    }

    // Проверка наличия имен игроков
    public void isEmpty() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка ввода");
        alert.setContentText("У некоторых игроков нет имени!");
        alert.showAndWait();
    }

    // Показ ошибки при совпадении имен игроков
    public void showDuplicateNameError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText("Ошибка ввода");
        alert.setContentText("Ники игроков не должны совпадать!");
        alert.showAndWait();
    }
}
