package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.classes.User;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class AddScreen {
    private final Scene scene;
    private final User currentUser;
    public AddScreen(User currentUser) {
        this.currentUser = currentUser;
        Pane root = new Pane();
        root.getChildren().add(getAddChoice());

        scene = new Scene(root, Application.windowSize[0], Application.windowSize[1]);
        scene.getStylesheets().add(Application.class.getResource("stylesheets/addscreen.css").toString());
    }

    private VBox getAddChoice() {
        VBox addChoice = new VBox();
        addChoice.setId("addchoice");
        addChoice.setAlignment(Pos.CENTER);

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


        // Set spacing between buttons
        addChoice.setSpacing(10);
        addChoice.getChildren().addAll(addMeal, addSleep, addExercise);

        return addChoice;
    }

    private Pane addMealForm(User currentUser) {
        VBox mealForm = new VBox();
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

        Button sendForm = new Button("Add");

        mealForm.getChildren().addAll(txtName, txtType, txtCalories, txtProtein, txtFats, txtCarbs, sendForm);

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
        VBox sleepForm = new VBox();
        sleepForm.setId("sleepform");
        sleepForm.setAlignment(Pos.CENTER);

        ComboBox<String> txtQuality = new ComboBox<>();
        txtQuality.setPromptText("Quality: ");
        txtQuality.getItems().addAll("Good", "Bad");

        TextField txtDuration = new TextField();
        txtDuration.setPromptText("Duration: ");

        DatePicker txtDate = new DatePicker();
        txtDate.setPromptText("Date: ");

        Button sendForm = new Button("Add");

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


        sleepForm.getChildren().addAll(txtQuality, txtDuration, txtDate, sendForm);

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
        VBox exerciseForm = new VBox();
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

        Button sendForm = new Button("Add");

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

        exerciseForm.getChildren().addAll(txtName, txtMuscleGroup, txtSets, txtReps, sendForm);

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
}


