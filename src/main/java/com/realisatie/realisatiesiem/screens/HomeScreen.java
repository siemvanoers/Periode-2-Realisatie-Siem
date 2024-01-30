package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.classes.User;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.Node;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
        logo.setAlignment(Pos.CENTER);
        return logo;
    }

    private FlowPane getHeader() {
        FlowPane header = new FlowPane();
        header.setId("header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPrefSize(Application.windowSize[0] - navWidth, headerHeight);

        Text userText = new Text("Welcome: " + currentUser.getFirst_name() + " " + currentUser.getLast_name());
        userText.setId("usertext");

        header.getChildren().add(userText);
        return header;
    }

    private Pane getNavbar() {
        FlowPane navbar = new FlowPane();
        navbar.setId("navbar");
        navbar.setOrientation(Orientation.HORIZONTAL);
        navbar.setPadding(new Insets(60, 0, 0, 0));
        navbar.setPrefSize(navWidth, Application.windowSize[1] - headerHeight);
        navbar.getChildren().addAll(
                generateNavItems("Home"),
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

    private ScrollPane getMeals() {
        ScrollPane container = new ScrollPane();
        container.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        container.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox meals = new VBox();
        meals.setId("meals");
        meals.setPrefSize(infoWidth, infoHeight);
        meals.setAlignment(Pos.TOP_CENTER);
        container.setContent(meals);



        String query = "SELECT name, type, calories, protein, fats, carbs FROM meal WHERE user_id = " + currentUser.getId();

        try (Statement statement = Application.connection.getConnection().createStatement();
             ResultSet mealsResultSet = statement.executeQuery(query)) {

            while (mealsResultSet.next()) {
                String name = mealsResultSet.getString("name");
                String type = mealsResultSet.getString("type");
                int calories = mealsResultSet.getInt("calories");
                int protein = mealsResultSet.getInt("protein");
                int fats = mealsResultSet.getInt("fats");
                int carbs = mealsResultSet.getInt("carbs");

                Node mealItem = generateMealItem(name, type, calories, protein, fats, carbs);
                meals.getChildren().add(mealItem);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return container;
    }

    private Node generateMealItem(String name, String type, int calories, int protein, int fats, int carbs) {
        Text mealText = new Text("\nName: " + name + "\nType: " + type +
                "\nCalories: " + calories + "\nProtein: " + protein +
                "\nFats: " + fats + "\nCarbs: " + carbs);

        return mealText;
    }



    private ScrollPane getSleep() {
        ScrollPane container = new ScrollPane();
        container.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        container.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox sleep = new VBox();
        sleep.setId("sleep");
        sleep.setPrefSize(infoWidth, infoHeight);
        sleep.setAlignment(Pos.TOP_CENTER);
        container.setContent(sleep);

        String query = "SELECT quality, duration, sleep_date FROM sleep WHERE user_id = " + currentUser.getId();

        try (Statement statement = Application.connection.getConnection().createStatement();
             ResultSet sleepResultSet = statement.executeQuery(query)) {

            while (sleepResultSet.next()) {
                String quality = sleepResultSet.getString("quality");
                int duration = sleepResultSet.getInt("duration");
                String sleepDate = sleepResultSet.getString("sleep_date");

                Node sleepItem = generateSleepItem(quality, duration, sleepDate);
                sleep.getChildren().add(sleepItem);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return container;
    }

    private Node generateSleepItem(String quality, int duration, String sleepDate) {
        Text sleepText = new Text("\nQuality: " + quality + "\nDuration: " + duration +
                "\nDate: " + sleepDate);

        sleepText.getStyleClass().add("sleeptext");

        return sleepText;
    }

    private ScrollPane getExercises() {
        ScrollPane container = new ScrollPane();
        container.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        container.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox exercises = new VBox();
        exercises.setId("exercises");
        exercises.setPrefSize(infoWidth, infoHeight);
        container.setContent(exercises);

        String query = "SELECT name, muscle_group, sets, reps FROM exercise WHERE user_id = " + currentUser.getId();

        try (Statement statement = Application.connection.getConnection().createStatement();
             ResultSet exercisesResultSet = statement.executeQuery(query)) {

            while (exercisesResultSet.next()) {
                String exerciseName = exercisesResultSet.getString("name");
                String muscleGroup = exercisesResultSet.getString("muscle_group");
                int sets = exercisesResultSet.getInt("sets");
                int reps = exercisesResultSet.getInt("reps");

                Node exerciseItem = generateExerciseItem(exerciseName, muscleGroup, sets, reps);
                exercises.getChildren().add(exerciseItem);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }

        return container;
    }

    private Node generateExerciseItem(String exerciseName, String muscleGroup, int sets, int reps) {
        Text exerciseText = new Text("\nExercise: " + exerciseName + "\nMuscle Group: " + muscleGroup +
                "\nSets: " + sets + "\nReps: " + reps);

        exerciseText.getStyleClass().add("exercisename");

        return exerciseText;
    }



    public Scene getScene() {
        return scene;
    }

    private void showAddScreen() {
        Application.mainStage.setScene(new AddScreen().getScene());
    }
}
