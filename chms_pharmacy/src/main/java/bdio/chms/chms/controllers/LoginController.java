package bdio.chms.chms.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Node;
import bdio.chms.chms.dao.DatabaseConnection;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    public TextField emailField;

    @FXML
    public PasswordField passwordField;

    @FXML
    public TextField carePoint; // Hospital field

    @FXML
    public void handleLoginButtonAction(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        String hospital = carePoint.getText();

        if (email.isEmpty() || password.isEmpty() || hospital.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Main Error", "Please fill in all fields.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM pharmacien WHERE email = ? AND password = ? AND hospital = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            statement.setString(3, hospital);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Credentials are correct, navigate to dashboard.fxml
                loadDashboard(event);
            } else {
                showAlert(Alert.AlertType.ERROR, "Main Failed", "Invalid credentials. Please try again.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while connecting to the database:\n" + e.getMessage());
        }
    }

    public void loadDashboard(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bdio/chms/chms/fxml/dashboard.fxml"));
            Scene dashboardScene = new Scene(loader.load());

            // Get the current stage and set the new scene
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle("Dashboard");
            stage.show();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Unable to load the dashboard:\n" + e.getMessage());
        }
    }

    public void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
