import Repositories.ServerRepository;
import javafx.application.Application;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.pages.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class App extends Application {
    private boolean isLoggedIn = false;
    private boolean isLoaded = false;

    private Stage stage;
    private Scene appScene;

    private final LoginPage loginPage;
    private final LoaderScreen loaderScreen;
    private final HomePage homePage;

    private int counter = 0;

    public App() throws IOException {
        //new ServerRepository(this::stream);
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

            File loginCSS = new File("src/styles/login.css");
            File loadCSS = new File("src/styles/loader.css");
            File homeCSS = new File("src/styles/home.css");

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

    public void stream(BufferedImage image) {
        try {
            System.out.println(image.getWidth() + "x" + image.getHeight());

            String fileName = "frame" + ++counter + ".png";
            File imgFile = new File(fileName);

            ImageIO.write(image, "png", imgFile);
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
