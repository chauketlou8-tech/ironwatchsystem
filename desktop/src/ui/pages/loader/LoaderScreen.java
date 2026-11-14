package ui.pages.loader;

import Models.Employee;
import Models.User;
import Repositories.ServerRepository;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class LoaderScreen extends VBox {

    private final List<TaskList.task> tasks;
    private HBox progress_bar;

    private int count = 1;
    private final double width = 940.0;
    private final int minLoading = 3500;

    private final Consumer<List<User>> setUsers;
    private final Consumer<Boolean> setLoaded;
    private final Consumer<List<Employee>> setEmployees;
    private final Runnable updateScene;

    public LoaderScreen(Consumer<Boolean> setLoaded, Consumer<List<User>> setUsers, Consumer<List<Employee>> setEmployees, Runnable updateScene) {
        this.setUsers = setUsers;
        this.setEmployees = setEmployees;
        this.setLoaded = setLoaded;
        this.updateScene = updateScene;

        TaskList taskList = new TaskList();

        String[] tks = {"connecting", "loading users", "loading employees", "ready"};

        for (String task : tks) {
            taskList.addToStart(task);
        }
        this.tasks = taskList.toList();
        tasks.getFirst().isCurrent = true;

        this.initUI();

        Platform.runLater(() -> {
            try {
                this.updateProgress();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void initUI() {
        getChildren().clear();
        getStyleClass().add("loader-screen");
        setAlignment(Pos.CENTER);
        setSpacing(28);
        setPadding(new Insets(40, 300, 40, 300));

        Label iron_label = new Label("IRONWATCH");
        iron_label.getStyleClass().add("brand-title");

        Label secure_label = new Label("Secure workspace");
        secure_label.getStyleClass().add("brand-subtitle");

        VBox labels = new VBox(6, iron_label, secure_label);
        labels.setAlignment(Pos.CENTER);

        // subtle pulsing glow on the title
        FadeTransition glow = new FadeTransition(Duration.seconds(2), iron_label);
        glow.setFromValue(1.0);
        glow.setToValue(0.75);
        glow.setAutoReverse(true);
        glow.setCycleCount(Animation.INDEFINITE);
        glow.play();

        HBox progress_container = new HBox();
        progress_container.getStyleClass().add("progress-container");
        progress_container.setMaxWidth(Double.MAX_VALUE);

        this.progress_bar = new HBox();
        this.progress_bar.getStyleClass().add("progress-bar");

        // shimmer sweep across the fill
        Rectangle shimmer = new Rectangle(40, 8);
        shimmer.getStyleClass().add("progress-shimmer");
        shimmer.setTranslateX(-60);

        TranslateTransition sweep = new TranslateTransition(Duration.seconds(1.6), shimmer);
        sweep.setFromX(-60);
        sweep.setToX(600);
        sweep.setCycleCount(Animation.INDEFINITE);
        sweep.setInterpolator(Interpolator.LINEAR);
        sweep.play();

        progress_container.getChildren().addAll(progress_bar, shimmer);

        VBox tasks_box = new VBox(4);
        tasks_box.getStyleClass().add("task-list");

        if (!tasks.isEmpty()) {
            int index = 0;
            for (TaskList.task t : tasks) {
                HBox status = new HBox();
                status.setAlignment(Pos.CENTER_LEFT);
                status.setMinWidth(20);
                status.setPrefWidth(20);
                status.setMaxWidth(20);
                status.getStyleClass().add("task-status");

                if (t.isCurrent) {
                    ProgressIndicator spinner = new ProgressIndicator();
                    spinner.setPrefSize(14, 14);
                    spinner.setMinSize(14, 14);
                    spinner.setMaxSize(14, 14);
                    spinner.getStyleClass().add("task-spinner");
                    status.getChildren().add(spinner);
                } else if (t.done) {
                    Label tick = new Label("✓");
                    tick.getStyleClass().add("task-tick");
                    status.getChildren().add(tick);
                }

                Label name = new Label(t.name);
                name.getStyleClass().add("task-label");

                HBox task_line = new HBox(10.0, status, name);
                task_line.setAlignment(Pos.CENTER_LEFT);
                task_line.getStyleClass().add("task-line");

                if (t.isCurrent) task_line.getStyleClass().add("task-current");
                if (t.done)      task_line.getStyleClass().add("task-done");

                // staggered fade-in
                task_line.setOpacity(0);
                FadeTransition fade = new FadeTransition(Duration.millis(400), task_line);
                fade.setFromValue(0);
                fade.setToValue(1);
                fade.setDelay(Duration.millis(120L * index));
                fade.play();

                tasks_box.getChildren().add(task_line);
                index++;
            }
        }

        getChildren().addAll(labels, progress_container, tasks_box);
    }

    private void updateProgress() throws InterruptedException {
        new Thread(() -> {
            long start = System.currentTimeMillis();
            for (TaskList.task t : tasks) {
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (t.isCurrent) {
                    String name = t.name;

                    switch (name) {
                        case "connecting":
                            this.connect();
                            break;

                        case "loading users":
                            try {
                                this.load_users();
                            } catch (SQLException | IOException e) {
                                throw new RuntimeException(e);
                            }

                            break;

                        case "loading employees":
                            try {
                                this.load_employees();
                            } catch (SQLException | IOException e) {
                                throw new RuntimeException(e);
                            }

                            break;

                        case "ready":
                            this.ready(start);
                            break;
                    }

                    t.done = true;
                    t.isCurrent = false;

                    if (t.next != null) {
                        t.next.isCurrent = true;
                    }
                }

                double percentage = count * width / tasks.size();
                Platform.runLater(() -> {
                    this.initUI();
                    this.progress_bar.setPrefWidth(percentage);
                });

                count++;
            }
        }).start();
    }

    private void connect() {
        System.out.println("connecting...");
    }

    private void load_users() throws SQLException, IOException {
        List<User> users = ServerRepository.getUsers();
        setUsers.accept(users);
    }

    private void load_employees() throws SQLException, IOException {
        List<Employee> employees = ServerRepository.getEmployees();
        setEmployees.accept(employees);
    }

    private void ready(long start) {
        setLoaded.accept(true);
        long duration = System.currentTimeMillis() - start;

        Platform.runLater(() -> {

            if (duration >= minLoading) {
                double percentage = count * width / tasks.size();
                Platform.runLater(() -> {
                    this.initUI();
                    this.progress_bar.setPrefWidth(percentage);

                    PauseTransition pause = new PauseTransition(Duration.millis(1500));
                    pause.setOnFinished(_ -> updateScene.run());
                    pause.play();
                });
            }

            else {
                PauseTransition pause = new PauseTransition(Duration.millis(minLoading - duration));
                pause.setOnFinished(_ -> updateScene.run());
                pause.play();
            }
        });
    }

    private static class TaskList {
        private static class task { // node
            private final String name;
            private boolean done;
            private boolean isCurrent;
            private task next;

            task(String name, task next) {
                this.name = name;
                this.next = next;
                this.done = false;
                this.isCurrent = false;
            }

            @Override
            public String toString() {
                return "Name: " + this.name + ", Done: " + this.done + ", Current: " + this.isCurrent + ", Next: " + (this.next != null ? this.next.name : "null");
            }
        }

        private task head;

        TaskList() {
            this.head = null;
        }

        void addToStart(String name) {
            this.head = new task(name, this.head);
        }

        void reverse() {
            task prev = null;
            task curr = head;

            while (curr != null) {
                task t = curr.next;
                curr.next = prev;
                prev = curr;
                curr = t;
            }

            head = prev;
        }

        List<task> toList() {
            this.reverse();

            task position = this.head;
            List<task> tasks = new ArrayList<>();

            while (position != null) {
                tasks.add(position);
                position = position.next;
            }

            return tasks;
        }
    }
}
