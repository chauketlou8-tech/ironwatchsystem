package ui.components.layout;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SideBar extends VBox {
    public SideBar() {
        getChildren().add(new Label("Side Bar"));
    }
}
