package com.realisatie.realisatiesiem.screens;

import com.realisatie.realisatiesiem.Application;
import com.realisatie.realisatiesiem.DatabaseConn;
import com.realisatie.realisatiesiem.classes.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.realisatie.realisatiesiem.Application.connection;
import static com.realisatie.realisatiesiem.Application.scenes;

public class LoginScreen {
    private final Scene scene;

    public LoginScreen() {

        FlowPane container = new FlowPane();
        container.setMinSize(Application.windowSize[0], Application.windowSize[1]);
        container.setAlignment(Pos.CENTER);


        VBox loginform = new VBox(10);
        loginform.setAlignment(Pos.CENTER);

        Text login = new Text("Login");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        HBox links = new HBox();
        links.setSpacing(50);

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            handleLoginForm(username, password);
        });

        Button register = new Button("Register");
        register.setOnAction(e -> {
            loginform.getChildren().clear();
            loginform.getChildren().add(getRegisterForm());
        });

        links.getChildren().addAll(loginButton, register);
        loginform.getChildren().addAll(login, username, password, links);


        container.getChildren().add(loginform);
        scene = new Scene(container);
    }

    private VBox getRegisterForm() {
        VBox registerForm = new VBox(10);
        registerForm.setAlignment(Pos.CENTER);

        Text register = new Text("Register");

        TextField firstName = new TextField();
        firstName.setPromptText("First Name");

        TextField lastName = new TextField();
        lastName.setPromptText("Last Name");

        TextField username = new TextField();
        username.setPromptText("Username");

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        Button registerUser = new Button("Register");
        registerUser.setOnAction(e -> {
            handleRegisterForm(firstName, lastName, username, password);
        });

        registerForm.getChildren().addAll(register, firstName, lastName, username, password, registerUser);

        return registerForm;
    }

    private void handleLoginForm(TextField usernameField, PasswordField passwordField) {
        String enteredUsername = usernameField.getText();
        String enteredPassword = passwordField.getText();
        User u = null;

        if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Information incomplete", "Please fill in all fields");
            return;
        }

        try (ResultSet users = getUsers()) {
            boolean loginSuccessful = false;

            while (users.next()) {
                String storedUsername = users.getString("username");
                String storedPassword = users.getString("password");

                if (enteredUsername.equals(storedUsername) && enteredPassword.equals(storedPassword)) {
                    // Login successful
                    loginSuccessful = true;
                    u = new User(storedUsername, storedPassword);
                    break;
                }
            }

            if (loginSuccessful) {
                showHomeScreen(u);
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid credentials", "Please check your username and password");
                // Clear fields or perform other actions after unsuccessful login
            }
        } catch (SQLException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(ex);
        }
    }

    private ResultSet getUsers() {
        try {
            ResultSet user = Application.connection.query("SELECT * FROM user");
            return user;
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Database Error", "Error accessing the database");
            throw new RuntimeException(e);
        }
    }

    private void handleRegisterForm(TextField firstName, TextField lastName, TextField username, PasswordField password) {
        if (firstName.getText().isEmpty() || lastName.getText().isEmpty() || username.getText().isEmpty() || password.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Information incomplete", "Please fill in all fields");
            return;
        }

        if (isUsernameTaken(username.getText())) {
            showAlert(Alert.AlertType.ERROR, "Error", "Username is taken", "Please choose a different username");
            return;
        }

        String query = "INSERT INTO user (first_name, last_name, username, password) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, firstName.getText());
            preparedStatement.setString(2, lastName.getText());
            preparedStatement.setString(3, username.getText());
            preparedStatement.setString(4, password.getText());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        showHomeScreen(new User(username.getText(), password.getText()));
    }

    private boolean isUsernameTaken(String username) {
        String query = "SELECT * FROM user WHERE username = ?";
        try (PreparedStatement preparedStatement = connection.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, username);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // If the result set has a next row, the username is taken
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error checking if username is taken", e);
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public Scene getScene() {
        return scene;
    }

    private void showHomeScreen(User user) {
        Application.mainStage.setScene(new HomeScreen(user).getScene());
    }
}
