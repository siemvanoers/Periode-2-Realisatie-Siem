package com.realisatie.realisatiesiem.screens;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class HomeScreen {
    private final Scene scene;


    public HomeScreen() {
        Pane root = new Pane();
        scene = new Scene(root, 320, 240);
    }



    public Scene getScene() {
        return scene;
    }
}
