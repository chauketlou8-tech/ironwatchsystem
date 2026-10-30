import javafx.application.Application;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.pages.*;

import java.io.File;
import java.nio.file.Path;
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
        //Parent root = !isLoaded ? this.loaderScreen : (isLoggedIn ? this.homePage : this.loginPage);
        Parent root = this.loginPage;

        if (this.appScene == null) {
            this.appScene = new Scene(root);

            File loginCSS = new File("desktop/src/styles/login.css");
            File loadCSS = new File("desktop/src/styles/loader.css");
            File homeCSS = new File("desktop/src/styles/home.css");

            this.appScene.getStylesheets().add(loginCSS.toURI().toString());
            this.appScene.getStylesheets().add(loadCSS.toURI().toString());
            this.appScene.getStylesheets().add(homeCSS.toURI().toString());

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
