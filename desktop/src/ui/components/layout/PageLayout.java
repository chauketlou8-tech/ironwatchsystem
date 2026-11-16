package ui.components.layout;

import Models.User;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class PageLayout extends VBox {
    public PageLayout(User user, Consumer<Boolean> setLoggedIn, Consumer<User> setCurrentUser, Consumer<String> setSearchText, Consumer<String> setCurrDashboard, Consumer<String> setCurrTab, Runnable updateScene, StringProperty curr_dashboard) {
        TopBar topbar = new TopBar(user, setLoggedIn, setCurrentUser, setSearchText, setCurrDashboard, updateScene);
        MiddleContent content = new MiddleContent(setCurrTab, curr_dashboard);

        VBox.setVgrow(content, Priority.ALWAYS);

        getChildren().addAll(topbar, content);
    }
}