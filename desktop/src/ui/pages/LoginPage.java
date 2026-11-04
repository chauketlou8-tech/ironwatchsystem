package ui.pages;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.function.Consumer;

public class LoginPage extends VBox {
    private final Button sign_in_button;

    private final TextField email_field;
    private final PasswordField password_field;

    private Consumer<Boolean> setLoggedIn;

    public LoginPage(Consumer<Boolean> setLoggedIn) {
        this();
        this.setLoggedIn = setLoggedIn;
    }
    private LoginPage() {
        StackPane root = new StackPane();
        root.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

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

        right_panel.setMaxSize(700,  Double.MAX_VALUE);
        right_panel.setPadding(new Insets(80, 150, 80, 150));

        Label iron_label = new Label("IRONWATCH");
        Label desc_label = new Label("Protecting what matters.");

        VBox form = new VBox();
        form.getStyleClass().add("form");

        form.setSpacing(10);
        form.setPadding(new Insets(30));

        VBox label_box = new VBox();

        Label welcome_label = new Label("Welcome back");
        welcome_label.getStyleClass().add("welcome_label");

        Label sign_label = new Label("Sign in to your workspace");
        sign_label.getStyleClass().add("sign_label");

        label_box.getChildren().addAll(welcome_label, sign_label);

        VBox input_box = new VBox();

        VBox email_box = new VBox();
        VBox password_box = new VBox();

        Label email_label = new Label("Email address");
        Label password_label = new Label("Password");

        email_label.getStyleClass().add("email_label");
        password_label.getStyleClass().add("password_label");

        this.email_field = new TextField();
        this.password_field = new PasswordField();

        this.email_field.setPadding(new Insets(8));
        this.password_field.setPadding(new Insets(8));

        this.email_field.setPromptText("johnmarguire@ironwatch.co.za");

        email_box.getChildren().addAll(email_label, this.email_field);
        password_box.getChildren().addAll(password_label, this.password_field);

        password_box.setSpacing(5);
        email_box.setSpacing(5);

        input_box.getChildren().addAll(email_box, password_box);
        input_box.setSpacing(10);

        HBox remember_box = new HBox();

        CheckBox checkbox = new CheckBox("Remember me");
        checkbox.getStyleClass().add("checkbox");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label forgot_label = new Label("Forgot Password?");
        forgot_label.getStyleClass().add("forgot_label");

        remember_box.getChildren().addAll(checkbox, spacer, forgot_label);

        this.sign_in_button= new Button("Sign in");
        this.sign_in_button.getStyleClass().add("button");

        this.sign_in_button.setMaxSize(Double.MAX_VALUE, 40);
        this.sign_in_button.setPadding(new Insets(10));

        this.sign_in_button.setOnAction(_ -> this.signin());

        form.getChildren().addAll(label_box, input_box, remember_box, this.sign_in_button);
        form.setSpacing(20);

        left_panel.getChildren().addAll(iron_label, desc_label);
        right_panel.getChildren().add(form);

        container.getChildren().addAll(left_panel, right_panel);
        root.getChildren().add(container);

        VBox.setVgrow(root, Priority.ALWAYS);

        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        getChildren().add(root);
    }

    private void signin() {
        long start =  System.currentTimeMillis();
        int min_loading = 3500;

        this.sign_in_button.setDisable(true);
        this.sign_in_button.setText("signing in");

        String[] credentials = this.getCredentials();
        String email  = credentials[0];
        String password = credentials[1];

        if (email.isEmpty() || password.isEmpty()){
            this.reset();
            return;
        }

        if (!email.endsWith("@ironwatch.co.za")) {
            this.reset();
            return;
        }

        long delta_time = System.currentTimeMillis() - start;

        if (delta_time > min_loading) {
            this.reset();
            return;
        }

        else {
            while(true) {
                long rn =  System.currentTimeMillis();
                if (rn - start >= min_loading) {
                    break;
                }
            }
        }

        this.reset();
        this.setLoggedIn.accept(true);
    }

    private String[] getCredentials() {
        String email = this.email_field.getText();
        String password = this.password_field.getText();

        return new String[]{email, password};
    }

    private void reset() {
        this.sign_in_button.setDisable(false);
        this.sign_in_button.setText("sign in");

        this.email_field.clear();
        this.password_field.clear();
    }
}
