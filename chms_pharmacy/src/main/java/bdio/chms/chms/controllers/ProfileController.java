package bdio.chms.chms.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import bdio.chms.chms.dao.DatabaseConnection;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfileController {

    @FXML
    public TextField firstNameField;

    @FXML
    public TextField lastNameField;

    @FXML
    public TextField emailField;

    @FXML
    public TextField phoneField;

    @FXML
    public VBox sidebar;

    // File path constants for FXML files
    public static final String DASHBOARD_VIEW = "/bdio/chms/chms/fxml/dashboard.fxml";
    public static final String PROFILE_VIEW = "/bdio/chms/chms/fxml/profile.fxml";
    public static final String MEDICATION_VIEW = "/bdio/chms/chms/fxml/Medicament.fxml";
    public static final String ORDONNANCE_VIEW = "/bdio/chms/chms/fxml/Ordonnance.fxml";
    public static final String LOGIN_VIEW = "/bdio/chms/chms/fxml/login-view.fxml";
    public static final String Fournisseur_VIEW= "/bdio/chms/chms/fxml/fournisseur.fxml";
    // Initialize the profile with data from the database
    public void initialize() {
        int defaultPharmacistID = 1; // Example: Replace with dynamic ID fetching logic if needed
        loadPharmacistInfo(defaultPharmacistID);
    }

    // Fetch and populate pharmacist information
    public void loadPharmacistInfo(int pharmacistID) {
        String query = "SELECT Prenom, Nom, email, Tel FROM pharmacien WHERE IDPharmacien = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, pharmacistID);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Populate fields with fetched data
                    firstNameField.setText(resultSet.getString("Prenom"));
                    lastNameField.setText(resultSet.getString("Nom"));
                    emailField.setText(resultSet.getString("email"));
                    phoneField.setText(resultSet.getString("Tel"));
                } else {
                    System.err.println("No pharmacist found with ID: " + pharmacistID);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading pharmacist data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Generic method for handling view transitions
    public void loadView(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            // Replace the current scene with the new one
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlFile);
            e.printStackTrace();
        }
    }

    // Handlers for sidebar navigation
    @FXML
    public void goToDashboard(ActionEvent event) {
        loadView(DASHBOARD_VIEW, event);
    }

    @FXML
    public void goToProfile(ActionEvent event) {
        loadView(PROFILE_VIEW, event);
    }

    @FXML
    public void showViewMedication(ActionEvent event) {
        loadView(MEDICATION_VIEW, event);
    }

    @FXML
    public void goToOrdonnance(ActionEvent event) {
        loadView(ORDONNANCE_VIEW, event);
    }




    @FXML
    public void goToLogin(javafx.event.ActionEvent event) {
        loadView(LOGIN_VIEW, event);
    }

    @FXML
    public void goToFournisseur(javafx.event.ActionEvent event) {
        loadView(Fournisseur_VIEW, event);
    }
}
