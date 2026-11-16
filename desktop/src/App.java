import Models.Employee;
import Models.User;
import javafx.application.Application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.pages.home.HomePage;
import ui.pages.loader.LoaderScreen;
import ui.pages.login.LoginPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App extends Application {
    private boolean isLoggedIn = false;
    private boolean isLoaded = false;

    private Stage stage;
    private Scene appScene;

    private final LoginPage loginPage;
    private final LoaderScreen loaderScreen;
    private HomePage homePage;

    private User currentUser;

    private final List<User> users = new ArrayList<>();
    private final List<Employee> employees = new ArrayList<>();

    private int counter = 0;

    private final StringProperty search_text;
    private final StringProperty curr_dashboard;
    private final StringProperty curr_tab;

    public App() {
        this.search_text = new SimpleStringProperty(null);
        this.curr_dashboard = new SimpleStringProperty(null);
        this.curr_tab = new SimpleStringProperty(null);

        this.loaderScreen = new LoaderScreen(this::setLoaded, this::setUsers, this::setEmployees, this::updateScene);
        this.loginPage = new LoginPage(this.users, this::setCurrentUser, this::setLoggedIn, this::updateScene);
        this.homePage = new HomePage(
                this.employees,
                this::setLoggedIn,
                this::setCurrentUser,
                this::setSearchText,
                this::setCurrDashboard,
                this::setCurrTab,
                this::updateScene,
                this.currentUser,
                this.curr_dashboard
        );
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

    public void updateScene() {
        if (this.stage == null) {
            return;
        }

        this.homePage = new HomePage(
                this.employees,
                this::setLoggedIn,
                this::setCurrentUser,
                this::setSearchText,
                this::setCurrDashboard,
                this::setCurrTab,
                this::updateScene,
                this.currentUser,
                this.curr_dashboard
        );

        Parent root = !isLoaded ? this.loaderScreen : (isLoggedIn ? this.homePage : this.loginPage);
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

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setUsers(List<User> users) {
        if (users == null) return;

        this.users.addAll(users);
    }

    public void setEmployees(List<Employee> employees) {
        if  (employees == null) return;

        this.employees.addAll(employees);
    }

    private void setSearchText(String searchText) {
        if (searchText == null || searchText.isBlank()) return;
        this.search_text.set(searchText);
    }

    private void setCurrDashboard(String currDashboard) {
        if (currDashboard == null || currDashboard.isBlank()) return;
        this.curr_dashboard.set(currDashboard);
    }

    private void setCurrTab(String currTab) {
        if (currTab == null || currTab.isBlank()) return;
        this.curr_tab.set(currTab);
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
