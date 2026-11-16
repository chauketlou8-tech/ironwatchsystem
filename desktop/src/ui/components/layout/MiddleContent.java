package ui.components.layout;

import javafx.beans.property.StringProperty;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.function.Consumer;

public class MiddleContent extends HBox {
    public MiddleContent(Consumer<String> setCurrTab, StringProperty curr_dashboard) {
        SideBar sidebar = new SideBar(setCurrTab, curr_dashboard);
        Content content = new Content();

        VBox.setVgrow(sidebar, Priority.ALWAYS);
        content.setMaxHeight(Double.MAX_VALUE);

        VBox.setVgrow(content, Priority.ALWAYS);
        content.setMaxHeight(Double.MAX_VALUE);

        getChildren().addAll(sidebar, content);
    }
}
