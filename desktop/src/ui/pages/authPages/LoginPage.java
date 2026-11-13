package ui.pages.authPages;

import Models.User;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.List;
import java.util.function.Consumer;

public class LoginPage extends VBox {
    private final Button sign_in_button;

    private final TextField email_field;
    private final PasswordField password_field;

    private final Label email_error;
    private final Label password_error;

    private List<User> users;

    private Consumer<Boolean> setPassEntered;
    private Consumer<Parent> setScene;
    private Consumer<User> setCurrentUser;

    private Runnable updateScene;

    public LoginPage(List<User> users, Consumer<Boolean> setPassEntered,  Consumer<Parent> setScene, Consumer<User> setCurrentUser,  Runnable updateScene) {
        this();
        this.users = users;
        this.setPassEntered = setPassEntered;
        this.setScene = setScene;
        this.updateScene = updateScene;
        this.setCurrentUser = setCurrentUser;
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

        Label iron_label = new Label("I R O N W A T C H");
        iron_label.getStyleClass().add("iron_label");

        Label desc_label = new Label("Protecting what matters.");
        desc_label.getStyleClass().add("desc_label");

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

        this.email_error = new Label();
        this.email_error.getStyleClass().add("email_error");

        this.password_error = new Label();
        this.password_error.getStyleClass().add("password_error");

        email_box.getChildren().addAll(email_label, this.email_field, email_error);
        password_box.getChildren().addAll(password_label, this.password_field, password_error);

        password_box.setSpacing(5);
        email_box.setSpacing(5);

        input_box.getChildren().addAll(email_box, password_box);

        HBox remember_box = new HBox();

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label forgot_label = new Label("Forgot Password?");
        forgot_label.getStyleClass().add("forgot_label");
        forgot_label.setOnMouseClicked(_ -> this.forgot_password());

        remember_box.getChildren().addAll(spacer, forgot_label);

        HBox warning_box = new HBox();
        warning_box.getStyleClass().add("warning_box");

        Label warning_label = new Label("Shared company workstation. Your session remains active until you sign out or the terminal locks automatically.");
        warning_label.getStyleClass().add("warning_label");

        warning_box.getChildren().addAll(warning_label);

        this.sign_in_button= new Button("Sign in");
        this.sign_in_button.getStyleClass().add("button");

        this.sign_in_button.setMaxSize(Double.MAX_VALUE, 40);
        this.sign_in_button.setPadding(new Insets(10));

        this.sign_in_button.setOnAction(_ -> this.signin());

        VBox input_rem = new VBox();
        input_rem.getChildren().addAll(input_box, remember_box);

        form.getChildren().addAll(label_box, input_rem, warning_box, this.sign_in_button);
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
        this.clear_errors();
        long start =  System.currentTimeMillis();
        int min_loading = 3500;

        this.sign_in_button.setDisable(true);
        this.sign_in_button.setText("signing in");

        new Thread(() -> {
            boolean isValid = this.authenticate();

            if (!isValid) {
                return;
            }

            //time passed from the start
            long delta_time = System.currentTimeMillis() - start;

            if (delta_time > min_loading) {
                Platform.runLater(this::reset);
                this.setPassEntered.accept(true);
                this.updateScene.run();
                return;
            }

            try {
                Thread.sleep(min_loading - delta_time);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Platform.runLater(() -> {
                this.reset();
                this.setPassEntered.accept(true);
                this.updateScene.run();
            });
        }).start();
    }

    private String[] getCredentials() {
        String email = this.email_field.getText().trim();
        String password = this.password_field.getText().trim();

        return new String[]{email, password};
    }

    private void reset() {
        this.sign_in_button.setDisable(false);
        this.sign_in_button.setText("sign in");

        this.email_field.clear();
        this.password_field.clear();
    }

    private void forgot_password() {
        ForgotPasswordPage forgotPasswordPage = new  ForgotPasswordPage();
        this.setScene.accept(forgotPasswordPage);
    }

    private void clear_errors() {
        this.email_error.setText("");
        this.password_error.setText("");
    }

    private boolean authenticate() {
        String[] credentials = this.getCredentials();
        String email  = credentials[0];
        String password = credentials[1];

        if (email.isBlank()){
            return this.fail(this.email_error, "Please enter your email.");
        }

        if (password.isBlank()){
            return this.fail(this.password_error, "Please enter your password.");
        }

        if (!email.endsWith("@ironwatch.co.za")) {
            return fail(this.email_error, "Please enter a valid email.");
        }

        User foundUser = this.users != null ? this.users.stream().filter(u -> u.email().equalsIgnoreCase(email)).findFirst().orElse(null) : null;

        if (foundUser == null) {
            return fail(this.email_error, "Invalid email.");
        }

        String hashed_password = foundUser.password().substring(2);
        byte[] hashed_password_bytes = HexFormat.of().parseHex(hashed_password);
        String hash = new String(hashed_password_bytes, StandardCharsets.UTF_8);

        if (hash.startsWith("$2b$")) {
            hash = "$2a$" + hash.substring(4);
        }

        boolean match = BCrypt.checkpw(password, hash);

        if (!match) {
            return this.fail(this.password_error, "Wrong password.");
        }

        this.setCurrentUser.accept(foundUser);
        return true;
    }

    private boolean fail(Label label, String message) {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(_ -> this.clear_errors());

        Platform.runLater(() -> {
            label.setText(message);
            pause.play();
            this.reset();
        });

        return false;
    }
}
