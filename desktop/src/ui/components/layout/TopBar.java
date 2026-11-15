package ui.components.layout;

import Models.User;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;

public class TopBar extends HBox {
    private final User user;
    private javafx.stage.Popup active_popup = null;
    private String dashboard;

    private final Consumer<Boolean> setLoggedIn;
    private final Consumer<User> setCurrentUser;
    private final Consumer<String> setSearchText;
    private final Runnable updateScene;

    int new_notifications = 0;

    public TopBar(User user, Consumer<Boolean> setLoggedIn, Consumer<User> setCurrentUser, Consumer<String> setSearchText, Runnable updateScene) {
        this.user = user;
        this.setLoggedIn = setLoggedIn;
        this.setCurrentUser = setCurrentUser;
        this.setSearchText = setSearchText;
        this.updateScene = updateScene;
        this.unitUI();
    }

    public void unitUI() {
        getStyleClass().add("top-bar");

        HBox left_panel = new HBox(15);
        left_panel.getStyleClass().add("left-panel");
        left_panel.setAlignment(Pos.CENTER_LEFT);

        FontAwesomeIcon shield_icon = new FontAwesomeIcon();
        shield_icon.setIcon(FontAwesomeIcons.SHIELD);
        shield_icon.setSize("18");
        shield_icon.getStyleClass().add("shield-icon");

        StackPane shield_box = new StackPane();
        shield_box.getStyleClass().add("shield-box");
        shield_box.getChildren().add(shield_icon);

        shield_box.setMaxHeight(45);
        shield_box.setMinHeight(45);
        shield_box.setMaxWidth(45);
        shield_box.setMinWidth(45);

        left_panel.getChildren().add(shield_box);

        HBox search_panel = new HBox();
        search_panel.getStyleClass().add("search-panel");
        search_panel.setAlignment(Pos.CENTER);

        TextField search_field = new TextField();
        search_field.setPromptText("Search clients, sites, guards, invoices,...");
        search_field.getStyleClass().add("search-field");
        search_field.setPrefWidth(350);

        search_field.textProperty().addListener((_, _, newValue) -> setSearchText.accept(newValue));

        FontAwesomeIcon search_icon = new FontAwesomeIcon();
        search_icon.setIcon(FontAwesomeIcons.SEARCH);
        search_icon.setSize("14");
        search_icon.getStyleClass().add("search-icon");

        StackPane search_container = new StackPane();
        search_container.setAlignment(Pos.CENTER_LEFT);
        search_container.getChildren().addAll(search_field, search_icon);

        StackPane.setMargin(search_icon, new Insets(0, 0, 0, 12));

        search_panel.getChildren().add(search_container);

        HBox right_panel = new HBox();
        right_panel.getStyleClass().add("right-panel");
        right_panel.setAlignment(Pos.CENTER_RIGHT);

        HBox notifications_box = new HBox();
        notifications_box.getStyleClass().add("notification-box");

        notifications_box.setAlignment(Pos.BASELINE_CENTER);
        notifications_box.setMinHeight(45);
        notifications_box.setPrefHeight(45);
        notifications_box.setMaxHeight(45);

        FontAwesomeIcon bell_icon = new FontAwesomeIcon();
        bell_icon.setIcon(FontAwesomeIcons.BELL);
        bell_icon.setSize("18");
        bell_icon.getStyleClass().add("notification-bell");

        Circle bg = new Circle(4);
        bg.getStyleClass().add("notification-badge");
        bg.setVisible(new_notifications > 0);

        AnchorPane bell_container = new AnchorPane();
        bell_container.setPrefSize(18, 18);
        bell_container.getChildren().addAll(bell_icon, bg);

        AnchorPane.setTopAnchor(bell_icon, 4.0);
        AnchorPane.setLeftAnchor(bell_icon, 2.0);

        AnchorPane.setTopAnchor(bg, 2.0);
        AnchorPane.setRightAnchor(bg, 0.0);

        notifications_box.getChildren().add(bell_container);

        right_panel.getChildren().add(notifications_box);

        Region spacer1 = new Region();
        Region spacer2 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox.setHgrow(spacer2, Priority.ALWAYS);

        if (this.user != null) {
            String department = this.user.department();
            String position = this.user.position();

            boolean no_switch = department.equals("Administration and Support");

            switch (department) {
                case "Human Resources":
                    this.dashboard = "Human Resources";
                    break;

                case "Executive Management":
                    this.dashboard = "Executive";
                    break;

                case "Administration and Support":
                    this.dashboard = "Administration and Support";
                    break;

                case "Administration and Management":
                    this.dashboard = "Management";
                    break;

                case "Security Operations":
                    List<String> security_operations = Arrays.asList("Security Operations Supervisor", "Security Supervisor", "Senior Security Officer", "Site Supervisor");
                    List<String> control_rooms_and_cctv = Arrays.asList("CCTV Operator", "Control Room Supervisor", "Control Room Operator");

                    if (security_operations.contains(position)) {
                        this.dashboard = "Security Operations";
                        break;
                    }

                    if (control_rooms_and_cctv.contains(position)) {
                        this.dashboard = "Control Rooms & CCTV";
                        break;
                    }
            }

            VBox labels_box = new VBox();
            labels_box.setAlignment(Pos.CENTER_LEFT);
            labels_box.setSpacing(2);

            Label logo_label = new Label("IRONWATCH");
            logo_label.getStyleClass().add("logo-label");

            Label dep_label = new Label(department);
            dep_label.getStyleClass().add("department-label");

            labels_box.getChildren().addAll(logo_label, dep_label);

            Label left_arrow = new Label("⌄");
            left_arrow.getStyleClass().add("dropdown-arrow");

            HBox left_container = new HBox(10);
            left_container.setAlignment(Pos.CENTER_LEFT);
            left_container.getStyleClass().add("hover-container");

            if (no_switch) {
                left_container.getChildren().addAll(labels_box);
            }

            else {
                left_container.getChildren().addAll(labels_box, left_arrow);
                left_container.setOnMouseClicked(_ -> this.open_dashboards_view(left_container, department, position));
            }

            left_panel.getChildren().addAll(left_container);

            String image_file = new File(this.user.profile_picture_path()).toURI().toString();
            Image image = new Image(image_file, false);

            double size = 45;
            ImageView imageView = new ImageView(image);

            double width = image.getWidth();
            double height = image.getHeight();
            double minDimension = Math.min(width, height);

            if (minDimension > 0) {
                double x = (width - minDimension) / 2;
                double y = (height - minDimension) / 2;
                imageView.setViewport(new Rectangle2D(x, y, minDimension, minDimension));
            }

            imageView.setFitWidth(size);
            imageView.setFitHeight(size);
            imageView.setPreserveRatio(false);
            imageView.setSmooth(true);

            Circle clip = new Circle(size / 2, size / 2, size / 2);
            imageView.setClip(clip);

            Circle border = new Circle(size / 2, size / 2, size / 2);
            border.setStroke(Color.web("#ff8a1f"));
            border.setStrokeWidth(2.0);
            border.setFill(Color.TRANSPARENT);

            StackPane avatarContainer = new StackPane(imageView, border);
            avatarContainer.setAlignment(Pos.CENTER);
            StackPane.setMargin(avatarContainer, new Insets(0, 0, 0, 10));

            VBox text_box = new VBox();
            text_box.setAlignment(Pos.CENTER_LEFT);
            text_box.setSpacing(2);

            Label name_label = new Label(this.user.firstName() + " " + this.user.lastName());
            name_label.getStyleClass().add("user-name");

            Label email_label = new Label(this.user.email());
            email_label.getStyleClass().add("user-email");

            text_box.getChildren().addAll(name_label, email_label);

            Label right_arrow = new Label("⌄");
            right_arrow.getStyleClass().add("dropdown-arrow");

            HBox user_container = new HBox(10);
            user_container.setAlignment(Pos.CENTER_RIGHT);
            user_container.getStyleClass().add("hover-container");

            user_container.setOnMouseClicked(_ -> this.open_profile_view(user_container));

            user_container.getChildren().addAll(avatarContainer, text_box, right_arrow);

            right_panel.getChildren().addAll(user_container);
        }

        getChildren().addAll(left_panel, spacer1, search_panel, spacer2, right_panel);
    }

    private void open_dashboards_view(javafx.scene.Node anchor, String department, String position) {
        if (this.active_popup != null && this.active_popup.isShowing()) {
            this.active_popup.hide();
            this.active_popup = null;
            return;
        }

        VBox menu = new VBox(2);
        menu.getStyleClass().add("dropdown-menu");
        menu.setPadding(new javafx.geometry.Insets(5));

        DashBoard[] options = {new DashBoard("Executive", 1), new DashBoard("Management", 2),  new DashBoard("Human Resources", 3), new DashBoard("Security Operations", 4),  new DashBoard("Control Rooms & CCTV", 5), new DashBoard("Administration & Support", 6)};

        for (DashBoard option : options) {
            HBox item = new HBox();
            item.getStyleClass().add("dropdown-item");
            item.setMaxWidth(Double.MAX_VALUE);
            item.setPrefWidth(250);

            //DO NOT TOUCH THIS EVER!!
            if (option.name.equalsIgnoreCase(this.dashboard)) {
                item.getStyleClass().add("Administration & Support");
            }

            switch (department) {
                case "Administration and Management":
                    if (option.name.equals("Executive")) continue;
                    break;

                case "Human Resources":
                    if (option.name.equals("Executive") || option.name.equals("Management")) continue;
                    break;

                case "Security Operations":
                    List<String> security_operations = Arrays.asList("Security Operations Supervisor", "Security Supervisor", "Senior Security Officer", "Site Supervisor");
                    List<String> control_rooms_and_cctv = Arrays.asList("CCTV Operator", "Control Room Supervisor", "Control Room Operator");

                    if (security_operations.contains(position)) {
                        if (option.name.equals("Executive") || option.name.equals("Management") || option.name.equals("Human Resources")) continue;
                        break;
                    }

                    if (control_rooms_and_cctv.contains(position)) {
                        if (option.name.equals("Executive") || option.name.equals("Management") || option.name.equals("Human Resources") || option.name.equals("Security Operations")) continue;
                        break;
                    }
            }

            VBox left_box = new VBox();

            Label name_label = new Label(option.getName());
            name_label.getStyleClass().add("option-name");

            Label level = new Label(option.getLevel());
            level.getStyleClass().add("option-level");

            left_box.getChildren().addAll(name_label, level);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            Circle active_dot = new Circle(4);
            active_dot.getStyleClass().add("active-dot");

            if (!option.name.equalsIgnoreCase(this.dashboard)) {
                active_dot.setVisible(false);
            }

            item.getChildren().addAll(left_box, spacer, active_dot);

            item.setOnMouseClicked(_ -> {
                this.dashboard = option.name;
                this.active_popup.hide();
                this.active_popup = null;
            });

            menu.getChildren().add(item);
        }

        javafx.stage.Popup popup = new javafx.stage.Popup();
        popup.getContent().add(menu);
        popup.setAutoHide(true);

        javafx.geometry.Bounds bounds = anchor.localToScreen(anchor.getBoundsInLocal());

        double x = bounds.getMinX() + 5;
        double y = bounds.getMaxY() + 5;

        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        if (y + 150 > screenBounds.getMaxY()) {
            y = bounds.getMinY() - 155;
        }

        popup.show(anchor, x, y);

        popup.setOnHidden(_ -> active_popup = null);
        active_popup = popup;
    }

    private void open_profile_view(javafx.scene.Node anchor) {
        if (this.active_popup != null && this.active_popup.isShowing()) {
            this.active_popup.hide();
            this.active_popup = null;
            return;
        }

        VBox menu = new VBox(2);
        menu.getStyleClass().add("dropdown-menu");
        menu.setPadding(new javafx.geometry.Insets(5));

        ProfileItem[] options = {new ProfileItem("My Profile", FontAwesomeIcons.USER), new ProfileItem("Company Density", FontAwesomeIcons.BUILDING), new ProfileItem("Sign out", FontAwesomeIcons.SIGN_OUT)};

        for (ProfileItem option : options) {
            HBox item = new HBox(10);
            item.getStyleClass().add("dropdown-item");
            item.setMaxWidth(Double.MAX_VALUE);
            item.setPrefWidth(260);
            item.setAlignment(Pos.CENTER_LEFT);

            FontAwesomeIcon icon = new FontAwesomeIcon();
            icon.setIcon(option.icon);
            icon.setSize("14");
            icon.getStyleClass().add("profile-icon");

            Label name_label = new Label(option.name);
            name_label.getStyleClass().add("option-name");

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            item.getChildren().addAll(icon, name_label, spacer);

            if (option.name.equals("Sign out")) {
                name_label.getStyleClass().add("logout-text");
                icon.getStyleClass().add("logout-icon");
            }

            item.setOnMouseClicked(_ -> {
                active_popup.hide();
                active_popup = null;

                if (option.name.equalsIgnoreCase("Sign out")) {
                    this.setLoggedIn.accept(false);
                    this.setCurrentUser.accept(null);

                    Platform.runLater(this.updateScene);
                }
            });

            menu.getChildren().add(item);
        }

        javafx.stage.Popup popup = new javafx.stage.Popup();
        popup.getContent().add(menu);
        popup.setAutoHide(true);

        javafx.geometry.Bounds bounds = anchor.localToScreen(anchor.getBoundsInLocal());

        double x = bounds.getMaxX() - 200;
        double y = bounds.getMaxY() + 5;

        javafx.geometry.Rectangle2D screenBounds = javafx.stage.Screen.getPrimary().getVisualBounds();
        if (y + 150 > screenBounds.getMaxY()) {
            y = bounds.getMinY() - 155;
        }

        popup.show(anchor, x, y);

        popup.setOnHidden(_ -> active_popup = null);
        active_popup = popup;
    }

    private record DashBoard(String name, int level) {
        private String getName() {
            return name + "  Dashboard";
        }

        private String getLevel() {
            return "Level " + level + " workspace";
        }
    }

    private record ProfileItem(String name, FontAwesomeIcons icon) {
    }
}
