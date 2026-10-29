import javafx.application.Application;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.pages.*;

import java.util.Objects;

public class App extends Application {
    private boolean isLoggedIn = false;
    private boolean isLoaded = false;

    private Stage stage;
    private Scene appScene;

    private final LoginPage loginPage;
    private final LoaderScreen loaderScreen;
    private final HomePage homePage;

    public App() {
        this.loginPage = new LoginPage(this::setLoggedIn);
        this.loaderScreen = new LoaderScreen(this::setLoaded);
        this.homePage = new HomePage(this::setLoggedIn);
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.updateScene();

        stage.setTitle("Ironwatch Security Management System");

        stage.setMaximized(true);
        stage.setFullScreenExitHint("");
        stage.getIcons().clear();

        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(true);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void updateScene() {
        Parent root = !isLoaded ? this.loaderScreen : (isLoggedIn ? this.homePage : this.loginPage);

        if (this.appScene == null) {
            this.appScene = new Scene(root);

//            this.appScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style/login.css")).toExternalForm());
//            this.appScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/loader.css")).toExternalForm());
//            this.appScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/home.css")).toExternalForm());

            this.stage.setScene(this.appScene);
        }
        else {
            this.appScene.setRoot(root);
        }
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public void setLoaded(boolean isLoaded) {
        this.isLoaded = isLoaded;
    }
}
