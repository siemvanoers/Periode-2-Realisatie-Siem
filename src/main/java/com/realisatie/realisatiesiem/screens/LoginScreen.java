package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.DatabaseConn;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginScreen {
    private final Scene scene;

    public LoginScreen() {

        FlowPane container = new FlowPane();
        container.setMinSize(Application.windowSize[0], Application.windowSize[1]);
        container.setAlignment(Pos.CENTER);

        scene = new Scene(container);
    }

    public Scene getScene() {
        return scene;
    }
}
