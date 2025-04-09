module ru.nstu.rgz {
    requires javafx.controls;
    requires javafx.media;
    opens ru.nstu.rgz to javafx.fxml;
    exports ru.nstu.rgz;
}
