package ui.components.layout;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcons;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class SideBar extends VBox {
    private final StringProperty curr_dashboard;

    private final Consumer<String> setCurrTab;

    private Label dashboard_label;

    public SideBar(Consumer<String> setCurrTab, StringProperty curr_dashboard) {
        this.setCurrTab = setCurrTab;

        this.curr_dashboard = curr_dashboard;

        this.initUI();
    }

    private void initUI() {
        getStyleClass().add("Side-bar");

        VBox currDash = new VBox();

        this.dashboard_label = new Label(this.curr_dashboard.get() + " Dashboard");
        this.dashboard_label.getStyleClass().add("dashboard-label");

        currDash.getChildren().addAll(new Label("Active dashboard".toUpperCase()), dashboard_label);

        getChildren().addAll(currDash, new Separator(Orientation.HORIZONTAL));

        this.curr_dashboard.addListener((_, _, newValue) -> this.updateUI(newValue));

        this.getDashboard();
    }

    private void updateUI(String newDashboard) {
        if (newDashboard.isBlank()) return;

        this.dashboard_label.setText(newDashboard + " Dashboard");
        this.getDashboard();
    }

    public static Tab[] executive_tabs = {
            new Tab("Dashboard", "Overview", FontAwesomeIcons.DASHBOARD, true),
            new Tab("Clients", "Business", FontAwesomeIcons.LAPTOP, false),
            new Tab("Contracts", "Business", FontAwesomeIcons.FILE_TEXT, false),
            new Tab("Finance", "Business", FontAwesomeIcons.MONEY, false),
            new Tab("Reports & Analytics", "Insight & Control", FontAwesomeIcons.LINE_CHART, false)
    };

    public static Tab[] management_tabs = {
            new Tab("Dashboard", "Overview", FontAwesomeIcons.DASHBOARD, true),
            new Tab("Clients", "Business", FontAwesomeIcons.LAPTOP, false),
            new Tab("Contracts", "Business", FontAwesomeIcons.NEWSPAPER_ALT, false),
            new Tab("Finance", "Business", FontAwesomeIcons.DOLLAR, false),
            new Tab("Schedular", "Field Operations", FontAwesomeIcons.CLOCK_ALT, false),
            new Tab("Security & Incidents", "Field Operations", FontAwesomeIcons.EXCLAMATION_TRIANGLE, false),
            new Tab("Assets", "Field Operations", FontAwesomeIcons.CUBE, false),
            new Tab("Reports & Analytics", "Insight & Control", FontAwesomeIcons.LINE_CHART, false)
    };

    public static Tab[] hr_tabs = {
            new Tab("HR Overview", "Overview", FontAwesomeIcons.DASHBOARD, true),
            new Tab("Employees", "People", FontAwesomeIcons.USERS, false),
            new Tab("Positions", "People", FontAwesomeIcons.SITEMAP, false),
            new Tab("Leave", "People", FontAwesomeIcons.CLOCK_ALT, false),
            new Tab("Payroll", "People", FontAwesomeIcons.MONEY, false),
            new Tab("Recruitment", "People", FontAwesomeIcons.USER_PLUS, false),
            new Tab("Training & Compliance", "People", FontAwesomeIcons.GRADUATION_CAP, false)
    };

    public static Tab[] security_operations_tabs = {
            new Tab("Security Overview", "Overview", FontAwesomeIcons.SHIELD, true),
            new Tab("Schedular", "Field Operations", FontAwesomeIcons.CLOCK_ALT, false),
            new Tab("Deployment", "Field Operations", FontAwesomeIcons.MAP_MARKER, false),
            new Tab("Attendance", "Field Operations", FontAwesomeIcons.CLOCK_ALT, false),
            new Tab("Patrols", "Field Operations", FontAwesomeIcons.STREET_VIEW, false),
            new Tab("Sites", "Field Operations", FontAwesomeIcons.BUILDING, false),
            new Tab("Security & Incidents", "Field Operations", FontAwesomeIcons.EXCLAMATION_TRIANGLE, false),
            new Tab("Assets", "Field Operations", FontAwesomeIcons.CUBES, false)
    };

    public static Tab[] control_room_tabs = {
            new Tab("Live Control Room", "Overview", FontAwesomeIcons.DESKTOP, true),
            new Tab("Security & Incidents", "Field Operations", FontAwesomeIcons.EXCLAMATION_TRIANGLE, false),
            new Tab("CCTV Monitoring", "Control Systems", FontAwesomeIcons.VIDEO_CAMERA, false),
            new Tab("Access Control", "Control Systems", FontAwesomeIcons.KEY, false),
            new Tab("Alarms & Alerts", "Control Systems", FontAwesomeIcons.BELL, false),
            new Tab("Security Logs", "Control Systems", FontAwesomeIcons.CLIPBOARD, false)
    };

    public static Tab[] admin_tabs = {
            new Tab("Administration Overview", "Overview", FontAwesomeIcons.DASHBOARD, true),
            new Tab("Users", "Administration", FontAwesomeIcons.USERS, false),
            new Tab("Roles & Permissions", "Administration", FontAwesomeIcons.SHIELD, false),
            new Tab("Audit Log", "Administration", FontAwesomeIcons.HISTORY, false),
            new Tab("Company Settings", "Administration", FontAwesomeIcons.COG, false),
            new Tab("Support Desk", "Administration", FontAwesomeIcons.HEADPHONES, false)
    };

    private void getDashboard() {
        if (this.curr_dashboard.get() == null) return;
        getChildren().remove(2, getChildren().size());

        int index = 0;

        String[] poss_dashboards = {"Executive", "Management", "Human Resources", "Security Operations", "Control Rooms & CCTV", "Administration & Support"};

        List<Tab[]> overall_tabs = new ArrayList<>();

        overall_tabs.add(executive_tabs);
        overall_tabs.add(management_tabs);
        overall_tabs.add(hr_tabs);

        overall_tabs.add(security_operations_tabs);
        overall_tabs.add(control_room_tabs);
        overall_tabs.add(admin_tabs);

        Map<String, DashBoard> dashBoardMap = new HashMap<>();

        for (String dashboard : poss_dashboards) {
            DashBoard dashBoard = new DashBoard(dashboard, overall_tabs.get(index++));
            dashBoardMap.put(dashBoard.name.toLowerCase(), dashBoard);
        }

        VBox dashboard_container = new VBox();
        DashBoard display_dashboard = dashBoardMap.get(this.curr_dashboard.get().toLowerCase());
        Tab[] display_tabs = display_dashboard.tabs();
        List<String> categories = this.getDistinctCategories(display_tabs);

        for (Tab tab : display_tabs) {
            VBox tab_box = new VBox();
            tab_box.getStyleClass().add("tab-box");

            if (tab.is_active) {
                tab_box.getStyleClass().add("active");
                this.setCurrTab.accept(tab.name);
            }

            if (categories.contains(tab.category.toLowerCase())) {
                Label category_label = new Label(tab.category);
                category_label.getStyleClass().add("category-label");
                tab_box.getChildren().add(category_label);
                categories.remove(tab.category.toLowerCase());
            }

            FontAwesomeIcon icon = new FontAwesomeIcon();
            icon.setIcon(tab.icon);
            icon.setSize("14");
            icon.getStyleClass().add("tab-icon");

            Label tab_label = new Label(tab.name);
            tab_label.getStyleClass().add("tab-name");

            HBox tab_container = new HBox(10);
            tab_container.setAlignment(Pos.CENTER_LEFT);
            tab_container.getStyleClass().add("tab-container");
            tab_container.getChildren().addAll(icon, tab_label);

            tab_container.setOnMouseClicked(_ -> {
                for (Tab t : display_tabs) {
                    if (t.is_active) {
                        t.is_active = false;
                    }
                }

                tab.is_active = true;
                this.setCurrTab.accept(tab.name);

                Platform.runLater(() -> this.updateUI(this.curr_dashboard.get()));
            });

            tab_box.getChildren().add(tab_container);

            getChildren().add(tab_box);
        }

        getChildren().add(dashboard_container);
    }

    private List<String> getDistinctCategories(Tab[] tabs) {
        if (tabs == null || tabs.length == 0) return new ArrayList<>();
        List<String> result = new ArrayList<>();

        for (Tab tab : tabs) {
            if (result.contains(tab.category.toLowerCase())) continue;
            result.add(tab.category.toLowerCase());
        }

        return result;
    }

    private record DashBoard(String name, Tab[] tabs) {
    }

    public static class Tab {
        private final String name;
        private final String category;
        private final FontAwesomeIcons icon;
        private boolean is_active;

        private Tab(String name, String category, FontAwesomeIcons icon, boolean is_active) {
            this.name = name;
            this.category = category;
            this.icon = icon;
            this.is_active = is_active;
        }
    }
}
