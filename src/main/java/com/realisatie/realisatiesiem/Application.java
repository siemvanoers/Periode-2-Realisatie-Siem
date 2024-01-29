package com.realisatie.realisatiesiem;

import com.realisatie.realisatiesiem.screens.LoginScreen;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.HashMap;

public class Application extends javafx.application.Application {

    public static DatabaseConn connection;
    public static Stage mainStage;
    public static HashMap<String, Scene> scenes = new HashMap<>();
    public static int[] windowSize = {2560, 1440};

    @Override
    public void start(Stage stage) throws IOException {
        connection = new DatabaseConn("localhost", "8889", "HFTSystem", "root", "root");
        connection.getConnection();

        scenes.put("loginscreen", new LoginScreen().getScene());

        mainStage = stage;

        mainStage.setTitle("Health and Fitness Tracking System!");
        mainStage.setScene(scenes.get("loginscreen"));
        mainStage.setWidth(windowSize[0]);
        mainStage.setHeight(windowSize[1]);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}