package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.classes.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class UDExerciseScreen {
    private final Scene scene;
    private final User currentUser;
    private final String exerciseName;
    private final String muscleGroup;
    private final int sets;
    private final int reps;

    public UDExerciseScreen(User currentUser, String exerciseName, String muscleGroup, int sets, int reps) {
        this.currentUser = currentUser;
        this.exerciseName = exerciseName;
        this.muscleGroup = muscleGroup;
        this.sets = sets;
        this.reps = reps;

        Pane root = new Pane();
        root.setId("root");
        root.getChildren().add(getForm());

        scene = new Scene(root, Application.windowSize[0], Application.windowSize[1]);
        scene.getStylesheets().add(Application.class.getResource("stylesheets/udscreens.css").toString());
    }

    private FlowPane getForm() {
        FlowPane content = new FlowPane();
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(200, 0, 0, 500));

        VBox form = new VBox(10);
        form.setId("form");

        Label nameLabel = new Label("Exercise Name:");
        nameLabel.getStyleClass().add("labels");
        TextField nameTextField = new TextField(exerciseName);

        Label muscleGroupLabel = new Label("Muscle Group:");
        muscleGroupLabel.getStyleClass().add("labels");
        ComboBox<String> muscleGroupComboBox = new ComboBox<>();
        muscleGroupComboBox.getItems().addAll("Chest", "Legs", "Arms", "Back", "Core", "Shoulders");
        muscleGroupComboBox.setValue(muscleGroup);

        Label setsLabel = new Label("Sets:");
        setsLabel.getStyleClass().add("labels");
        TextField setsTextField = new TextField(String.valueOf(sets));

        Label repsLabel = new Label("Reps:");
        repsLabel.getStyleClass().add("labels");
        TextField repsTextField = new TextField(String.valueOf(reps));

        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);

        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            updateExercise(nameTextField.getText(), muscleGroupComboBox.getValue(), setsTextField.getText(), repsTextField.getText());;
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            deleteExercise();
            showHomeScreen();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            showHomeScreen();
        });

        buttonsBox.getChildren().addAll(updateButton, deleteButton, backButton);
        form.getChildren().addAll(nameLabel, nameTextField, muscleGroupLabel, muscleGroupComboBox, setsLabel, setsTextField, repsLabel, repsTextField, buttonsBox);
        content.getChildren().addAll(form);

        return content;
    }

    private void updateExercise(String newName, String newMuscleGroup, String setsText, String repsText) {
        // Check if any of the required fields are empty
        if (newName.isEmpty() || newMuscleGroup.isEmpty() || setsText.isEmpty() || repsText.isEmpty()) {
            // Show an alert indicating the missing fields
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        try {
            // Parse text fields to integers
            int newSets = Integer.parseInt(setsText);
            int newReps = Integer.parseInt(repsText);

            // Update exercise in the database
            Connection connection = Application.connection.getConnection();
            Statement statement = connection.createStatement();
            String query = "UPDATE exercise SET name = '" + newName + "', muscle_group = '" + newMuscleGroup + "', sets = " + newSets + ", reps = " + newReps + " WHERE name = '" + exerciseName + "'";
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Exercise updated successfully.");
                showHomeScreen(); // Only navigate to home screen if there are no errors
            } else {
                System.out.println("Failed to update exercise.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        } catch (NumberFormatException e) {
            // Show an alert if the user entered invalid numeric values for sets or reps
            showAlert("Error", "Please enter valid numeric values for Sets and Reps.");
        }
    }


    private void deleteExercise() {
        try {
            Connection connection = Application.connection.getConnection();
            Statement statement = connection.createStatement();
            String query = "DELETE FROM exercise WHERE name = '" + exerciseName + "'";
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Exercise deleted successfully.");
            } else {
                System.out.println("Failed to delete exercise.");
            }
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
