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
    public static int[] windowSize = {2560, 1664};

    /**
     * Starts the application by setting up the database connection, initializing
     * the login screen, and configuring the main stage for the Health and Fitness
     * Tracking System.
     *
     * @param  stage  the primary stage for the application
     * @throws IOException  if there is an error during input/output operations
     */
    @Override
    public void start(Stage stage) throws IOException {
        connection = new DatabaseConn("adainforma.tk", "3306", "bp2_hftsystem", "hftsystem", "4rnT46z7#");
        connection.getConnection();

        scenes.put("loginscreen", new LoginScreen().getScene());

        mainStage = stage;

        mainStage.setTitle("Health and Fitness Tracking System!");
        mainStage.setScene(scenes.get("loginscreen"));
        mainStage.setWidth(windowSize[0]);
        mainStage.setHeight(windowSize[1]);

        mainStage.setFullScreen(false);
        mainStage.setMaximized(false);
        mainStage.setResizable(false);

        mainStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}