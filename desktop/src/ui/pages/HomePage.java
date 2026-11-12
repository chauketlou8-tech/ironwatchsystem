package ui.pages;

import Models.User;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class HomePage extends VBox {
    public HomePage(Consumer<Boolean> setLoggedIn, Consumer<User> setCurrentUser, Runnable updateScene, User user) {

    }
}
