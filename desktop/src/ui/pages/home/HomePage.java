package ui.pages.home;

import Models.Employee;
import Models.User;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import ui.components.layout.PageLayout;

import java.util.List;
import java.util.function.Consumer;

public class HomePage extends VBox {
    public HomePage(List<Employee> employees, Consumer<Boolean> setLoggedIn, Consumer<User> setCurrentUser, Consumer<String> setSearchText, Consumer<String> setCurrDashboard, Consumer<String> setCurrTab, Runnable updateScene, User user, StringProperty curr_dashboard) {
        PageLayout layout = new PageLayout(user, setLoggedIn, setCurrentUser, setSearchText, setCurrDashboard, setCurrTab, updateScene, curr_dashboard);

        VBox.setVgrow(layout, Priority.ALWAYS);

        getChildren().add(layout);
        getStyleClass().add("home-page");
    }
}
