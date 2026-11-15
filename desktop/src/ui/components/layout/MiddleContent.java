package ui.components.layout;

import javafx.scene.layout.HBox;

public class MiddleContent extends HBox {
    public MiddleContent() {
        getChildren().addAll(new SideBar(), new Content());
    }
}
