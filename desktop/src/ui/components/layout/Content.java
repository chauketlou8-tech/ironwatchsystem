package ui.components.layout;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class Content extends VBox {
    public Content() {
        getChildren().add(new Label("Content"));
    }
}
