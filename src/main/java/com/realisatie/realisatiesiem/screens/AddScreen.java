package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.classes.User;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static com.realisatie.realisatiesiem.screens.HomeScreen.headerHeight;
import static com.realisatie.realisatiesiem.screens.HomeScreen.navWidth;

public class AddScreen {
    private final Scene scene;
    private final User currentUser;

    public AddScreen(User currentUser) {
        this.currentUser = currentUser;
        Pane root = new Pane();
        GridPane content = new GridPane();

        content.add(getLogo(), 0, 0);
        content.add(getHeader(), 1, 0);
        content.add(getNavbar(), 0, 1);
        content.add(getAddChoice(), 1, 1);

        root.getChildren().addAll(content);
        scene = new Scene(root, Application.windowSize[0], Application.windowSize[1]);

        scene.getStylesheets().add(Application.class.getResource("stylesheets/AddScreen.css").toString());
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

    private FlowPane getAddChoice() {
        FlowPane addChoice = new FlowPane();
        addChoice.setId("addchoice");
        addChoice.setPadding(new Insets(250, 0, 0, 500));

        VBox buttons = new VBox(5);
        buttons.setId("buttons");
        buttons.setAlignment(Pos.CENTER);

        Button addMeal = new Button("Add Meal");
        addMeal.setOnAction(e -> {
            addChoice.getChildren().clear();
            addChoice.getChildren().add(addMealForm(currentUser));
        });

        Button addSleep = new Button("Add Sleep");
        addSleep.setOnAction(e -> {
            addChoice.getChildren().clear();
            addChoice.getChildren().add(addSleepForm(currentUser));
        });

        Button addExercise = new Button("Add Exercise");
        addExercise.setOnAction(e -> {
            addChoice.getChildren().clear();
            addChoice.getChildren().add(addExerciseForm(currentUser));
        });

        buttons.getChildren().addAll(addMeal, addSleep, addExercise);
        addChoice.getChildren().addAll(buttons);



        return addChoice;
    }

    private Pane addMealForm(User currentUser) {
        VBox mealForm = new VBox(10);
        mealForm.setId("mealform");
        mealForm.setAlignment(Pos.CENTER);

        TextField txtName = new TextField();
        txtName.setPromptText("Meal name: ");

        ComboBox<String> txtType = new ComboBox<>();
        txtType.setPromptText("Type: ");
        txtType.getItems().addAll("Breakfast", "Lunch", "Dinner", "Snack");

        TextField txtCalories = new TextField();
        txtCalories.setPromptText("Calories: ");

        TextField txtProtein = new TextField();
        txtProtein.setPromptText("Protein: ");

        TextField txtFats = new TextField();
        txtFats.setPromptText("Fats :");

        TextField txtCarbs = new TextField();
        txtCarbs.setPromptText("Carbs: ");

        HBox buttons = new HBox(80);
        Button sendForm = new Button("Add");

        Button cancelForm = new Button("Back");
        cancelForm.setOnAction(e -> {
            showAddScreen();
        });

        buttons.getChildren().addAll(sendForm, cancelForm);

        sendForm.setOnAction(event -> {
            // Retrieve values from text fields and combo box
            String name = txtName.getText();
            String type = txtType.getValue(); // Assuming your ComboBox is of type String
            String caloriesText = txtCalories.getText();
            String proteinText = txtProtein.getText();
            String fatsText = txtFats.getText();
            String carbsText = txtCarbs.getText();

            // Validate fields
            if (name.trim().isEmpty() || type == null || caloriesText.trim().isEmpty() ||
                    proteinText.trim().isEmpty() || fatsText.trim().isEmpty() || carbsText.trim().isEmpty()) {
                // Show alert for missing or invalid data
                showAlert("Error", "Please fill in all fields.");
            } else {
                // Parse text fields to integers
                int calories = Integer.parseInt(caloriesText);
                int protein = Integer.parseInt(proteinText);
                int fats = Integer.parseInt(fatsText);
                int carbs = Integer.parseInt(carbsText);

                // Call a method to insert data into the database
                addMealToDatabase(currentUser.getId(), name, type, calories, protein, fats, carbs);

                showHomeScreen();
            }
        });

        mealForm.getChildren().addAll(txtName, txtType, txtCalories, txtProtein, txtFats, txtCarbs, buttons);

        return mealForm;
    }

    private void addMealToDatabase(int userId, String name, String type, int calories, int protein, int fats, int carbs) {
        try (Connection connection = Application.connection.getConnection();
             Statement statement = connection.createStatement()) {

            String query = String.format(
                    "INSERT INTO meal (user_id, name, type, calories, protein, fats, carbs) VALUES (%d, '%s', '%s', %d, %d, %d, %d)",
                    userId, name, type, calories, protein, fats, carbs);

            statement.executeUpdate(query);
            System.out.println("Meal added to the database.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private Pane addSleepForm(User currentUser) {
        VBox sleepForm = new VBox(8);
        sleepForm.setId("sleepform");
        sleepForm.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> txtQuality = new ComboBox<>();
        txtQuality.setPromptText("Quality: ");
        txtQuality.getItems().addAll("Good", "Bad");

        TextField txtDuration = new TextField();
        txtDuration.setPromptText("Duration: ");

        DatePicker txtDate = new DatePicker();
        txtDate.setPromptText("Date: ");

        HBox buttons = new HBox(100);
        Button sendForm = new Button("Add");

        Button cancelForm = new Button("Back");
        cancelForm.setOnAction(e -> {
            showAddScreen();
        });

        buttons.getChildren().addAll(sendForm, cancelForm);

        sendForm.setOnAction(event -> {
            // Retrieve values from combo box, text fields, and date picker
            String quality = txtQuality.getValue(); // Assuming your ComboBox is of type String
            String durationText = txtDuration.getText();
            LocalDate date = txtDate.getValue();

            // Validate fields
            if (quality == null || durationText.trim().isEmpty() || date == null) {
                // Show alert for missing or invalid data
                showAlert("Error", "Please fill in all fields.");
            } else {
                // Parse text fields to integers
                int duration = Integer.parseInt(durationText);

                // Call a method to insert sleep data into the database
                addSleepToDatabase(currentUser.getId(), quality, duration, date.toString());

                showHomeScreen();
            }
        });


        sleepForm.getChildren().addAll(txtQuality, txtDuration, txtDate, buttons);

        return sleepForm;
    }

    private void addSleepToDatabase(int userId, String quality, int duration, String sleepDate) {
        try (Connection connection = Application.connection.getConnection();
             Statement statement = connection.createStatement()) {

            String query = String.format(
                    "INSERT INTO sleep (user_id, quality, duration, sleep_date) VALUES (%d, '%s', %d, '%s')",
                    userId, quality, duration, sleepDate);

            statement.executeUpdate(query);
            System.out.println("Sleep data added to the database.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private Pane addExerciseForm(User currentUser) {
        VBox exerciseForm = new VBox(6);
        exerciseForm.setId("exerciseform");
        exerciseForm.setAlignment(Pos.CENTER);

        TextField txtName = new TextField();
        txtName.setPromptText("Exercise name: ");

        ComboBox<String> txtMuscleGroup = new ComboBox<>();
        txtMuscleGroup.setPromptText("Type: ");
        txtMuscleGroup.getItems().addAll("Chest", "Legs", "Arms", "Back", "Core", "Shoulders");

        TextField txtSets = new TextField();
        txtSets.setPromptText("Sets: ");

        TextField txtReps = new TextField();
        txtReps.setPromptText("Reps: ");

        HBox buttons = new HBox(80);
        Button sendForm = new Button("Add");

        Button cancelForm = new Button("Back");
        cancelForm.setOnAction(e -> {
            showAddScreen();
        });

        buttons.getChildren().addAll(sendForm, cancelForm);

        sendForm.setOnAction(event -> {
            // Retrieve values from text fields and combo box
            String name = txtName.getText();
            String muscleGroup = txtMuscleGroup.getValue(); // Assuming your ComboBox is of type String
            String setsText = txtSets.getText();
            String repsText = txtReps.getText();

            // Validate fields
            if (name.isEmpty() || muscleGroup == null || setsText.trim().isEmpty() || repsText.trim().isEmpty()) {
                // Show alert for missing or invalid data
                showAlert("Error", "Please fill in all fields.");
            } else {
                // Parse text fields to integers
                int sets = Integer.parseInt(setsText);
                int reps = Integer.parseInt(repsText);

                // Call a method to insert exercise data into the database
                addExerciseToDatabase(currentUser.getId(), name, muscleGroup, sets, reps);

                showHomeScreen();
            }
        });

        exerciseForm.getChildren().addAll(txtName, txtMuscleGroup, txtSets, txtReps, buttons);

        return exerciseForm;
    }

    private void addExerciseToDatabase(int userId, String name, String muscleGroup, int sets, int reps) {
        try (Connection connection = Application.connection.getConnection();
             Statement statement = connection.createStatement()) {

            String query = String.format(
                    "INSERT INTO exercise (user_id, name, muscle_group, sets, reps) VALUES (%d, '%s', '%s', %d, %d)",
                    userId, name, muscleGroup, sets, reps);

            statement.executeUpdate(query);
            System.out.println("Exercise added to the database.");

        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public Scene getScene() {
        return scene;
    }

    private void showHomeScreen() {
        Application.mainStage.setScene(new HomeScreen(currentUser).getScene());
    }
    private void showAddScreen() {
        Application.mainStage.setScene(new AddScreen(currentUser).getScene());
    }
}


