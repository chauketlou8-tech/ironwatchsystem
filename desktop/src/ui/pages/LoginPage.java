package ui.pages;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.function.Consumer;

public class LoginPage extends VBox {
    public LoginPage(Consumer<Boolean> setLoggedIn) {
        StackPane root = new StackPane();
        root.setPrefHeight(Double.MAX_VALUE);
        root.setMaxHeight(Double.MAX_VALUE);

        HBox container = new HBox();
        container.getStyleClass().add("container");
        VBox.setVgrow(container, Priority.ALWAYS);
        container.setFillHeight(true);
        container.setMaxHeight(Double.MAX_VALUE);

        VBox left_panel = new VBox();
        left_panel.getStyleClass().add("left_panel");

        left_panel.setSpacing(10);
        left_panel.setPadding(new Insets(10));
        left_panel.setMaxHeight(Double.MAX_VALUE);

        HBox.setHgrow(left_panel, Priority.ALWAYS);
        VBox.setVgrow(left_panel, Priority.ALWAYS);

        left_panel.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox right_panel = new VBox();
        right_panel.getStyleClass().add("right_panel");

        right_panel.setSpacing(10);
        right_panel.setPadding(new Insets(10));

        HBox.setHgrow(right_panel, Priority.ALWAYS);
        VBox.setVgrow(right_panel, Priority.ALWAYS);

        right_panel.setMaxSize(Double.MAX_VALUE,  Double.MAX_VALUE);

        Label iron_label = new Label("IRONWATCH");
        Label desc_label = new Label("Protecting what matters.");

        VBox form = new VBox();

        Label welcome_label = new Label("Welcome back");
        Label sign_label = new Label("Sign in to your workspace");

        VBox email_box = new VBox();
        VBox password_box = new VBox();

        Label email_label = new Label("Email address");
        Label password_label = new Label("Password");

        TextField email_field = new TextField();
        PasswordField password_field = new PasswordField();

        email_box.getChildren().addAll(email_label, email_field);
        password_box.getChildren().addAll(password_label, password_field);

        form.getChildren().addAll(welcome_label, sign_label, email_box, password_box);

        left_panel.getChildren().addAll(iron_label, desc_label);
        right_panel.getChildren().add(form);

        container.getChildren().addAll(left_panel, right_panel);
        root.getChildren().add(container);
        getChildren().add(root);
    }
}
