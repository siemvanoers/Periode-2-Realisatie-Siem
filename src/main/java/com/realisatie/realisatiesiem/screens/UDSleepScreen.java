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
import java.time.LocalDate;

public class UDSleepScreen {
    private final Scene scene;
    private final User currentUser;
    private final String quality;
    private final int duration;
    private final String sleepDate;
    private DatePicker sleepDatePicker;

    public UDSleepScreen(User currentUser, String quality, int duration, String sleepDate) {
        this.currentUser = currentUser;
        this.quality = quality;
        this.duration = duration;
        this.sleepDate = sleepDate;

        Pane root = new Pane();
        root.setId("root");

        scene = new Scene(root, Application.windowSize[0], Application.windowSize[1]);
        scene.getStylesheets().add(Application.class.getResource("stylesheets/udscreens.css").toString());

        root.getChildren().add(getForm());
    }

    private FlowPane getForm() {

        FlowPane content = new FlowPane();
        content.setAlignment(Pos.CENTER);
        content.setHgap(10);
        content.setVgap(10);
        content.setPadding(new Insets(25, 25, 25, 25));

        VBox form = new VBox(8);
        form.setId("form");
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Update/Delete Sleep");
        sceneTitle.setId("sceneTitle");

        Label qualityLabel = new Label("Quality:");
        ComboBox<String> qualityComboBox = new ComboBox<>();
        qualityComboBox.getItems().addAll("Good", "Bad");
        qualityComboBox.setValue(quality);

        Label durationLabel = new Label("Duration:");
        TextField durationTextField = new TextField(String.valueOf(duration));

        Label sleepDateLabel = new Label("Date:");
        sleepDatePicker = new DatePicker(LocalDate.parse(sleepDate));

        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.CENTER);
        Button updateButton = new Button("Update");
        updateButton.setOnAction(e -> {
            updateSleep(qualityComboBox.getValue(), Integer.parseInt(durationTextField.getText()), sleepDatePicker.getValue().toString());
            showHomeScreen();
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            deleteSleep();
            showHomeScreen();
        });

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            showHomeScreen();
        });


        buttonsBox.getChildren().addAll(updateButton, deleteButton, backButton);
        form.getChildren().addAll(sceneTitle, qualityLabel, qualityComboBox, durationLabel, durationTextField, sleepDateLabel, sleepDatePicker, buttonsBox);
        content.getChildren().addAll(form);

        return content;
    }

    private void updateSleep(String newQuality, int newDuration, String newSleepDate) {
        String query = "UPDATE sleep SET quality = '" + newQuality + "', duration = " + newDuration +
                ", sleep_date = '" + newSleepDate + "' WHERE quality = '" + quality +
                "' AND duration = " + duration + " AND sleep_date = '" + sleepDate +
                "' AND user_id = " + currentUser.getId();

        try (Statement statement = Application.connection.getConnection().createStatement()) {
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Sleep record updated successfully!");
            } else {
                System.out.println("Failed to update sleep record.");
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private void deleteSleep() {
        String query = "DELETE FROM sleep WHERE quality = '" + quality + "' AND duration = " + duration +
                " AND sleep_date = '" + sleepDate + "' AND user_id = " + currentUser.getId();

        try (Statement statement = Application.connection.getConnection().createStatement()) {
            int rowsAffected = statement.executeUpdate(query);
            if (rowsAffected > 0) {
                System.out.println("Sleep record deleted successfully!");
            } else {
                System.out.println("Failed to delete sleep record.");
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
