package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.classes.User;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class HomeScreen {
    private final Scene scene;
    private final int headerHeight = 80;
    private final int navWidth = 200;
    private final int infoWidth = 350;
    private final int infoHeight = 600;
    public User currentUser;

    public HomeScreen(User user) {
        this.currentUser = user;
        Pane root = new Pane();
        GridPane content = new GridPane();


        content.add(getLogo(), 0, 0);
        content.add(getHeader(), 1, 0);
        content.add(getNavbar(), 0, 1);
        content.add(getUserInfo(), 1, 1);

        root.getChildren().addAll(content);
        scene = new Scene(root, Application.windowSize[0], Application.windowSize[1]);

        scene.getStylesheets().add(Application.class.getResource("stylesheets/homescreen.css").toString());
    }

    private FlowPane getLogo() {
        FlowPane logo = new FlowPane();
        logo.setId("logo");
        logo.setPrefSize(navWidth, headerHeight);
//        logo.setStyle("-fx-background-color: #e500ff;");
        logo.setAlignment(Pos.CENTER);
        return logo;
    }

    private FlowPane getHeader() {
        FlowPane header = new FlowPane();
        header.setId("header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefSize(Application.windowSize[0] - navWidth, headerHeight);
//        header.setStyle("-fx-background-color: #0039f3;");


        Label userLabel = new Label("User: " + currentUser.getFirst_name() + " " + currentUser.getLast_name());
        userLabel.setId("userLabel");

        header.getChildren().add(userLabel);
        return header;
    }

    private Pane getNavbar() {
        FlowPane navbar = new FlowPane();
        navbar.setId("navbar");
        navbar.setOrientation(Orientation.HORIZONTAL);
        navbar.setPadding(new Insets(60, 0, 0, 0));
        navbar.setPrefSize(navWidth, Application.windowSize[1] - headerHeight);
//        navbar.setStyle("-fx-background-color: #ff0000;");
        navbar.getChildren().addAll(
                generateNavItems("Home"),
                generateNavItems("Sleep"),
                generateNavItems("Exercises"),
                generateNavItems("Meals"),
                generateNavItems("Add")
        );

        return navbar;
    }

    private FlowPane generateNavItems(String title) {
        FlowPane navItems = new FlowPane();
        navItems.setId("navitems");
        navItems.setAlignment(Pos.CENTER);
        navItems.setPrefSize(navWidth, 50);
        navItems.setAlignment(Pos.CENTER);

        navItems.setOnMouseClicked(e -> {
            if (title.equals("Home")) {

            }
            if (title.equals("Sleep")) {

            }
            if (title.equals("Exercises")) {

            }
            if (title.equals("Meals")) {

            }
            if (title.equals("Add")) {
                showAddScreen();
            }
        });


        Text txtNavItem = new Text(title);
        navItems.getChildren().add(txtNavItem);

        return navItems;
    }

    private Pane getUserInfo() {
        Pane userInfo = new Pane();
        userInfo.setId("userinfo");
        userInfo.setPrefSize(100, 100);


        GridPane userInfoGrid = new GridPane();
        userInfoGrid.setPadding(new Insets(60, 0, 0, 60));
        userInfoGrid.setHgap(50);


        userInfoGrid.add(getMeals(), 0, 0);
        userInfoGrid.add(getSleep(), 1, 0);
        userInfoGrid.add(getExercises(), 2, 0);

        userInfo.getChildren().add(userInfoGrid);

        return userInfo;
    }

    private FlowPane getMeals() {
        FlowPane meals = new FlowPane();
        meals.setId("meals");
        meals.setPrefSize(infoWidth, infoHeight);

        return meals;
    }

    private FlowPane getSleep() {
        FlowPane sleep = new FlowPane();
        sleep.setId("sleep");
        sleep.setPrefSize(infoWidth, infoHeight);

        return sleep;
    }

    private FlowPane getExercises() {
        FlowPane exercises = new FlowPane();
        exercises.setId("exercises");
        exercises.setPrefSize(infoWidth, infoHeight);

        return exercises;
    }


    public Scene getScene() {
        return scene;
    }

    private void showAddScreen() {
        Application.mainStage.setScene(new AddScreen().getScene());
    }
}
