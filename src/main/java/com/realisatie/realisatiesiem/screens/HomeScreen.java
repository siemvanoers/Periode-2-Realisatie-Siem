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
    public static final int headerHeight = 80;
    public static final int navWidth = 200;
    public static final int infoWidth = 350;
    public static final int infoHeight = 600;
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
        logo.setAlignment(Pos.CENTER);
        logo.setId("logo");

        Text systemName = new Text("HFTSystem");
        systemName.setId("systemname");

        logo.setPrefSize(navWidth, headerHeight);
        logo.setAlignment(Pos.CENTER);
        logo.getChildren().add(systemName);

        return logo;
    }

    private FlowPane getHeader() {
        FlowPane header = new FlowPane();
        header.setId("header");
        header.setAlignment(Pos.CENTER_LEFT);
        header.setPadding(new Insets(0, 0, 0, 50));
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

        Text txtMeals = new Text("MEALS");
        txtMeals.getStyleClass().add("headertext");

        VBox meals = new VBox();
        meals.setId("meals");
        meals.setPrefSize(infoWidth, infoHeight);
        meals.setSpacing(10);
        meals.setAlignment(Pos.TOP_CENTER);

        meals.getChildren().add(txtMeals);
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
        VBox mealItem = new VBox();
        mealItem.getStyleClass().add("items");
        mealItem.setAlignment(Pos.CENTER);
        mealItem.setSpacing(10);

        Text nameLabel = new Text("Name: " + name);
        nameLabel.getStyleClass().add("labels");

        Text typeLabel = new Text("Type: " + type);
        typeLabel.getStyleClass().add("labels");

        Text caloriesLabel = new Text("Calories: " + calories);
        caloriesLabel.getStyleClass().add("labels");

        Text proteinLabel = new Text("Protein: " + protein);
        proteinLabel.getStyleClass().add("labels");

        Text fatsLabel = new Text("Fats: " + fats);
        fatsLabel.getStyleClass().add("labels");

        Text carbsLabel = new Text("Carbs: " + carbs);
        carbsLabel.getStyleClass().add("labels");

        mealItem.getChildren().addAll(nameLabel, typeLabel, caloriesLabel, proteinLabel, fatsLabel, carbsLabel);

        return mealItem;
    }


    private ScrollPane getSleep() {
        ScrollPane container = new ScrollPane();
        container.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        container.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Text txtSleep = new Text("SLEEP");
        txtSleep.getStyleClass().add("headertext");

        VBox sleep = new VBox();
        sleep.setId("sleep");
        sleep.setPrefSize(infoWidth, infoHeight);
        sleep.setSpacing(10);
        sleep.setAlignment(Pos.TOP_CENTER);

        sleep.getChildren().add(txtSleep);
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
        VBox sleepItem = new VBox();
        sleepItem.getStyleClass().add("items");
        sleepItem.setAlignment(Pos.CENTER);
        sleepItem.setSpacing(10);

        Text qualityLabel = new Text("Quality: " + quality);
        qualityLabel.getStyleClass().add("labels");

        Text durationLabel = new Text("Duration: " + duration);
        durationLabel.getStyleClass().add("labels");

        Text sleepDateLabel = new Text("Date: " + sleepDate);
        sleepDateLabel.getStyleClass().add("labels");

        sleepItem.getChildren().addAll(qualityLabel, durationLabel, sleepDateLabel);

        return sleepItem;
    }


    private ScrollPane getExercises() {
        ScrollPane container = new ScrollPane();
        container.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        container.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        Text txtExercises = new Text("EXERCISE");
        txtExercises.getStyleClass().add("headertext");

        VBox exercises = new VBox();
        exercises.setId("exercises");
        exercises.setPrefSize(infoWidth, infoHeight);
        exercises.setSpacing(10);
        exercises.setAlignment(Pos.TOP_CENTER);

        exercises.getChildren().add(txtExercises);
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
        VBox exerciseItem = new VBox();
        exerciseItem.getStyleClass().add("items");
        exerciseItem.setAlignment(Pos.CENTER);
        exerciseItem.setSpacing(10);

        Text exerciseLabel = new Text("Exercise: " + exerciseName);
        exerciseLabel.getStyleClass().add("labels");

        Text muscleGroupLabel = new Text("Muscle Group: " + muscleGroup);
        muscleGroupLabel.getStyleClass().add("labels");

        Text setsLabel = new Text("Sets: " + sets);
        setsLabel.getStyleClass().add("labels");

        Text repsLabel = new Text("Reps: " + reps);
        repsLabel.getStyleClass().add("labels");

        exerciseItem.getChildren().addAll(exerciseLabel, muscleGroupLabel, setsLabel, repsLabel);

        return exerciseItem;
    }



    public Scene getScene() {
        return scene;
    }

    private void showAddScreen() {
        Application.mainStage.setScene(new AddScreen(currentUser).getScene());
    }
}
