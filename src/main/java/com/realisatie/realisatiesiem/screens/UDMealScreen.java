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
        content.setPadding(new Insets(25, 25, 25, 25));

        VBox form = new VBox(10);
        form.setId("form");
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(25, 25, 25, 25));

        Label nameLabel = new Label("Meal Name:");
        TextField nameTextField = new TextField(mealName);

        Label typeLabel = new Label("Meal Type:");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Breakfast", "Snack", "Lunch", "Dinner");
        typeComboBox.setValue(mealType);

        Label caloriesLabel = new Label("Calories:");
        TextField caloriesTextField = new TextField(String.valueOf(calories));

        Label proteinLabel = new Label("Protein:");
        TextField proteinTextField = new TextField(String.valueOf(protein));

        Label fatsLabel = new Label("Fats:");
        TextField fatsTextField = new TextField(String.valueOf(fats));

        Label carbsLabel = new Label("Carbs:");
        TextField carbsTextField = new TextField(String.valueOf(carbs));

        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            updateMeal(
                    nameTextField.getText(),
                    typeComboBox.getValue(),
                    Integer.parseInt(caloriesTextField.getText()),
                    Integer.parseInt(proteinTextField.getText()),
                    Integer.parseInt(fatsTextField.getText()),
                    Integer.parseInt(carbsTextField.getText()));

            showHomeScreen();
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

        buttonsBox.getChildren().addAll(updateButton, deleteButton, backButton);
        form.getChildren().addAll(nameLabel, nameTextField, typeLabel, typeComboBox, caloriesLabel, caloriesTextField, proteinLabel, proteinTextField, fatsLabel, fatsTextField, carbsLabel, carbsTextField, buttonsBox);
        content.getChildren().addAll(form);

        return content;
    }

    private void updateMeal(String newName, String newType, int newCalories, int newProtein, int newFats, int newCarbs) {
        String query = "UPDATE meal SET name = '" + newName + "', type = '" + newType + "', calories = " + newCalories +
                ", protein = " + newProtein + ", fats = " + newFats + ", carbs = " + newCarbs +
                " WHERE name = '" + mealName + "' AND user_id = " + currentUser.getId();

        try (Statement statement = Application.connection.getConnection().createStatement()) {
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Meal updated successfully!");
            } else {
                System.out.println("Failed to update meal.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
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

    public Scene getScene() {
        return scene;
    }

    private void showHomeScreen() {
        Application.mainStage.setScene(new HomeScreen(currentUser).getScene());
    }
}

