package ui.components.layout;

import Models.User;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class PageLayout extends VBox {
    public PageLayout(User user, Consumer<Boolean> setLoggedIn, Consumer<User> setCurrentUser, Consumer<String> setSearchText, Runnable updateScene) {
        getChildren().addAll(new TopBar(user, setLoggedIn, setCurrentUser, setSearchText, updateScene), new MiddleContent());
    }
}