package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.classes.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
        content.setHgap(10);
        content.setVgap(10);
        content.setPadding(new Insets(25, 25, 25, 25));

        VBox form = new VBox(10);
        form.setId("form");
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(25, 25, 25, 25));

        Label nameLabel = new Label("Exercise Name:");
        TextField nameTextField = new TextField(exerciseName);

        Label muscleGroupLabel = new Label("Muscle Group:");
        ComboBox<String> muscleGroupComboBox = new ComboBox<>();
        muscleGroupComboBox.getItems().addAll("Chest", "Legs", "Arms", "Back", "Core", "Shoulders");
        muscleGroupComboBox.setValue(muscleGroup);

        Label setsLabel = new Label("Sets:");
        TextField setsTextField = new TextField(String.valueOf(sets));

        Label repsLabel = new Label("Reps:");
        TextField repsTextField = new TextField(String.valueOf(reps));

        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            updateExercise(nameTextField.getText(), muscleGroupComboBox.getValue(), Integer.parseInt(setsTextField.getText()), Integer.parseInt(repsTextField.getText()));
            showHomeScreen();
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

    private void updateExercise(String newName, String newMuscleGroup, int newSets, int newReps) {
        try {
            Connection connection = Application.connection.getConnection();
            Statement statement = connection.createStatement();
            String query = "UPDATE exercise SET name = '" + newName + "', muscle_group = '" + newMuscleGroup + "', sets = " + newSets + ", reps = " + newReps + " WHERE name = '" + exerciseName + "'";
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Exercise updated successfully.");
            } else {
                System.out.println("Failed to update exercise.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
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

    public Scene getScene() {
        return scene;
    }
    private void showHomeScreen() {
        Application.mainStage.setScene(new HomeScreen(currentUser).getScene());
    }
}
