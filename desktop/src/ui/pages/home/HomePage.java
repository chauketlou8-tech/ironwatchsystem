package ui.pages.home;

import Models.Employee;
import Models.User;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import ui.components.layout.PageLayout;

import java.util.List;
import java.util.function.Consumer;

public class HomePage extends VBox {
    public HomePage(List<Employee> employees, Consumer<Boolean> setLoggedIn, Consumer<User> setCurrentUser, Consumer<String> setSearchText, Runnable updateScene, User user) {
        getChildren().add(new PageLayout(user, setLoggedIn, setCurrentUser, setSearchText, updateScene));
        getStyleClass().add("home-page");
    }
}
