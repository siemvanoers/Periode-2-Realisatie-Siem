package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class AddScreen {
    private final Scene scene;
    public AddScreen() {
        Pane root = new Pane();


        scene = new Scene(root, Application.windowSize[0], 600);
    }

    public Scene getScene() {
        return scene;
    }
}
