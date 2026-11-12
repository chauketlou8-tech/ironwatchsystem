package ui.pages.authPages;

import Models.User;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class OTPScreen extends VBox {
    private final Runnable updateScene;
    private final Consumer<Boolean> setLoggedIn;
    private final Consumer<User> setCurrentUser;

    public OTPScreen(Consumer<Boolean> setLoggedIn, Runnable updateScene,  Consumer<User> setCurrentUser) {
        this.updateScene = updateScene;
        this.setLoggedIn = setLoggedIn;
        this.setCurrentUser = setCurrentUser;

        getChildren().add(new Label("OTP Screen"));
    }
}
