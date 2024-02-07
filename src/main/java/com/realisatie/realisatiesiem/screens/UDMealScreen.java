package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.classes.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.sql.Statement;

public class UDMealScreen {
    private final Scene scene;
    private final User currentUser;
    private final String mealName;
    private final String mealType;
    private final int calories;
    private final int protein;
    private final int fats;
    private final int carbs;

    public UDMealScreen(User currentUser, String mealName, String mealType, int calories, int protein, int fats, int carbs) {
        this.currentUser = currentUser;
        this.mealName = mealName;
        this.mealType = mealType;
        this.calories = calories;
        this.protein = protein;
        this.fats = fats;
        this.carbs = carbs;

        Pane root = new Pane();
        root.setId("root");

        root.getChildren().add(getForm());

        scene = new Scene(root, Application.windowSize[0], Application.windowSize[1]);
        scene.getStylesheets().add(Application.class.getResource("stylesheets/udscreens.css").toString());
    }

    private FlowPane getForm() {
        FlowPane content = new FlowPane();
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(200, 0, 0, 550));

        VBox form = new VBox(10);
        form.setId("form");

        Label nameLabel = new Label("Meal Name:");
        nameLabel.getStyleClass().add("labels");
        TextField nameTextField = new TextField(mealName);

        Label typeLabel = new Label("Meal Type:");
        typeLabel.getStyleClass().add("labels");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Breakfast", "Snack", "Lunch", "Dinner");
        typeComboBox.setValue(mealType);

        Label caloriesLabel = new Label("Calories:");
        caloriesLabel.getStyleClass().add("labels");
        TextField caloriesTextField = new TextField(String.valueOf(calories));

        Label proteinLabel = new Label("Protein:");
        proteinLabel.getStyleClass().add("labels");
        TextField proteinTextField = new TextField(String.valueOf(protein));

        Label fatsLabel = new Label("Fats:");
        fatsLabel.getStyleClass().add("labels");
        TextField fatsTextField = new TextField(String.valueOf(fats));

        Label carbsLabel = new Label("Carbs:");
        carbsLabel.getStyleClass().add("labels");
        TextField carbsTextField = new TextField(String.valueOf(carbs));

        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            updateMeal(nameTextField.getText(), typeComboBox.getValue(), caloriesTextField.getText(), proteinTextField.getText(), fatsTextField.getText(), carbsTextField.getText());
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            deleteMeal();
            showHomeScreen();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            showHomeScreen();
        });

        buttonsBox.getChildren().addAll(backButton, deleteButton, updateButton);
        form.getChildren().addAll(nameLabel, nameTextField, typeLabel, typeComboBox, caloriesLabel, caloriesTextField, proteinLabel, proteinTextField, fatsLabel, fatsTextField, carbsLabel, carbsTextField, buttonsBox);
        content.getChildren().addAll(form);

        return content;
    }

    private void updateMeal(String newName, String newType, String newCalories, String newProtein, String newFats, String newCarbs) {
        // Check if any of the required fields are empty
        if (newName.isEmpty() || newType.isEmpty() || newCalories.isEmpty() || newProtein.isEmpty() || newFats.isEmpty() || newCarbs.isEmpty()) {
            // Show an alert indicating the missing fields
            showAlert("Error", "Please fill in all fields.");
            return;
        }

        try {
            // Parse text fields to integers
            int calories = Integer.parseInt(newCalories);
            int protein = Integer.parseInt(newProtein);
            int fats = Integer.parseInt(newFats);
            int carbs = Integer.parseInt(newCarbs);

            // Update meal in the database
            String query = "UPDATE meal SET name = '" + newName + "', type = '" + newType + "', calories = " + calories +
                    ", protein = " + protein + ", fats = " + fats + ", carbs = " + carbs +
                    " WHERE name = '" + mealName + "' AND user_id = " + currentUser.getId();

            try (Statement statement = Application.connection.getConnection().createStatement()) {
                int rowsAffected = statement.executeUpdate(query);
                if (rowsAffected > 0) {
                    System.out.println("Meal updated successfully!");
                    showHomeScreen(); // Only navigate to home screen if there are no errors
                } else {
                    System.out.println("Failed to update meal.");
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        } catch (NumberFormatException e) {
            // Show an alert if the user entered invalid numeric values for calories, protein, fats, or carbs
            showAlert("Error", "Please enter valid numeric values for Calories, Protein, Fats, and Carbs.");
        }
    }
    private void deleteMeal() {
        String query = "DELETE FROM meal WHERE name = '" + mealName + "' AND user_id = " + currentUser.getId();

        try (Statement statement = Application.connection.getConnection().createStatement()) {
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Meal deleted successfully!");
            } else {
                System.out.println("Failed to delete meal.");
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

