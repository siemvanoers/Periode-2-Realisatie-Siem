package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.classes.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class HomeScreen {
    private final Scene scene;
    private int width = Application.windowSize[0];
    private int height = Application.windowSize[1];
    public User currentUser;

    public HomeScreen(User user) {
        this.currentUser = user;
        Pane root = new Pane();
        root.getChildren().addAll(getMenu());

        scene = new Scene(root, width, height);
    }

    private HBox getMenu() {
        HBox menu = new HBox();
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(10);

        Label userLabel = new Label("User: " + currentUser.getUsername());
        menu.getChildren().add(userLabel);

        menu.setSpacing(10);
        menu.setAlignment(Pos.CENTER);
        return menu;
    }

    public VBox sidebar() {
        VBox sidebar = new VBox();
        sidebar.setSpacing(10);
        sidebar.setAlignment(Pos.CENTER);

        



        return sidebar;
    }




    public Scene getScene() {
        return scene;
    }
}
