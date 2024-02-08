package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.classes.User;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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

    /**
     * Constructor for the HomeScreen class.
     * @param user The current user logged into the system.
     */
    public HomeScreen(User user) {
        this.currentUser = user;
        Pane root = new Pane();
        GridPane content = new GridPane();

        // Add logo, header, navbar, and user info to the content grid pane
        content.add(getLogo(), 0, 0);
        content.add(getHeader(), 1, 0);
        content.add(getNavbar(), 0, 1);
        content.add(getUserInfo(), 1, 1);

        root.getChildren().addAll(content);
        // Set the scene with the root pane and specified dimensions
        scene = new Scene(root, Application.windowSize[0], Application.windowSize[1]);

        // Add stylesheet to the scene
        scene.getStylesheets().add(Application.class.getResource("stylesheets/homescreen.css").toString());
    }


    /**
     * Creates the logo area of the screen.
     * @return FlowPane representing the logo area.
     */
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


    /**
     * Creates the header area of the screen displaying user information.
     * @return FlowPane representing the header area.
     */
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


    /**
     * Creates the navigation bar area of the screen.
     * @return Pane representing the navigation bar area.
     */
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

    /**
     * Generates navigation items for the navigation bar.
     * @param title The title of the navigation item.
     * @return FlowPane representing a navigation item.
     */
    private FlowPane generateNavItems(String title) {
        FlowPane navItems = new FlowPane();
        navItems.setId("navitems");
        navItems.setAlignment(Pos.CENTER);
        navItems.setPrefSize(navWidth, 50);
        navItems.setAlignment(Pos.CENTER);

        navItems.setOnMouseClicked(e -> {
            if (title.equals("Home")) {
                showHomeScreen();
            }
            if (title.equals("Add")) {
                showAddScreen();
            }
        });

        Text txtNavItem = new Text(title);
        navItems.getChildren().add(txtNavItem);

        return navItems;
    }

    /**
     * Creates the user information area of the screen.
     * @return Pane representing the user information area.
     */
    private Pane getUserInfo() {
        Pane userInfo = new Pane();
        userInfo.setId("userinfo");
        userInfo.setPrefSize(100, 100);

        GridPane userInfoGrid = new GridPane();
        userInfoGrid.setPadding(new Insets(60, 0, 0, 60));
        userInfoGrid.setHgap(50);

        // Add meals, sleep, and exercises sections to the user information grid
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

        // Create a VBox to hold meal items
        VBox meals = new VBox();
        meals.setId("meals");
        meals.setPrefSize(infoWidth, infoHeight);
        meals.setSpacing(10);
        meals.setAlignment(Pos.TOP_CENTER);

        meals.getChildren().add(txtMeals);
        container.setContent(meals);

        // Query to retrieve meal data from the database
        String query = "SELECT name, type, calories, protein, fats, carbs FROM meal WHERE user_id = " + currentUser.getId();

        try (Statement statement = Application.connection.getConnection().createStatement();
             ResultSet mealsResultSet = statement.executeQuery(query)) {

            // Iterate over the result set and populate the meals area
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


    /**
     * Generates a meal item for display in the meals area.
     * @param name The name of the meal.
     * @param type The type of the meal.
     * @param calories The calorie content of the meal.
     * @param protein The protein content of the meal.
     * @param fats The fat content of the meal.
     * @param carbs The carbohydrate content of the meal.
     * @return Node representing a meal item.
     */
    private Node generateMealItem(String name, String type, int calories, int protein, int fats, int carbs) {
        VBox mealItem = new VBox();
        mealItem.getStyleClass().add("items");
        mealItem.setAlignment(Pos.CENTER);
        mealItem.setSpacing(10);
        mealItem.setOnMouseClicked(e -> {
            showUDMealScreen(name, type, calories, protein, fats, carbs);
        });

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

    // Similar methods for sleep and exercises areas...


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
        sleepItem.setOnMouseClicked(e -> {
            showUDSleepScreen(quality, duration, sleepDate);
        });

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
        exerciseItem.setOnMouseClicked(e -> {
            showUDExerciseScreen(exerciseName, muscleGroup, sets, reps);
        });

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


    /**
     * Retrieves the scene associated with the HomeScreen.
     * @return Scene representing the HomeScreen.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Navigates to the AddScreen.
     */
    private void showAddScreen() {
        Application.mainStage.setScene(new AddScreen(currentUser).getScene());
    }

    /**
     * Refreshes and navigates to the HomeScreen.
     */
    private void showHomeScreen() {
        Application.mainStage.setScene(new HomeScreen(currentUser).getScene());
    }

    // Methods to navigate to screens for updating and deleting meal, sleep, and exercise items...

    private void showUDMealScreen(String mealName, String mealType, int calories, int protein, int carbs, int fats) {
        Application.mainStage.setScene(new UDMealScreen(currentUser, mealName, mealType, calories, protein, carbs, fats).getScene());
    }

    private void showUDSleepScreen(String quality, int duration, String sleepDate) {
        Application.mainStage.setScene(new UDSleepScreen(currentUser, quality, duration, sleepDate).getScene());
    }

    private void showUDExerciseScreen(String exerciseName, String muscleGroup, int sets, int reps) {
        Application.mainStage.setScene(new UDExerciseScreen(currentUser, exerciseName, muscleGroup, sets, reps).getScene());
    }

}
