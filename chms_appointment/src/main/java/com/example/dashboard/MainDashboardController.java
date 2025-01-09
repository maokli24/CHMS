package com.example.dashboard;

import bdio.chms.chms.views.Main;
import com.example.doctorappointments.MainApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class MainDashboardController {

    @FXML
    public void handleMainApplicationButton() {
        FXMLLoader loader = new FXMLLoader(MainDashboard.class.getResource("/com/example/doctorappointments/first-interface.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create a new stage
        Stage stage = new Stage();
        stage.setTitle("Appointment");
        stage.setScene(new Scene(root));
        stage.show();
        //loadNewModule("/com/example/doctorappointments/first-interface.fxml", "chms_appointment");
    }

    @FXML
    public void handleMainPharmacyButton() {
        URL resource = getClass().getClassLoader().getResource("login-view.fxml");
        if (resource == null) {
            System.out.println("FXML file not found: " + "/bdio/chms/chms/fxml/login-view.fxml");
            return;
        }
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/bdio/chms/chms/fxml/login-view.fxml"));

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Create a new stage
        Stage stage = new Stage();
        stage.setTitle("Pharmacy");
        stage.setScene(new Scene(root));
        stage.show();
    }

    private void loadNewModule(String fxmlPath, String title) {
        try {
            // Load the new FXML using ClassLoader
            URL resource = getClass().getClassLoader().getResource(fxmlPath);
            if (resource == null) {
                System.out.println("FXML file not found: " + fxmlPath);
                return;
            }
            FXMLLoader loader = new FXMLLoader(resource);

            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to load module");
            alert.setContentText(e.getMessage());
            alert.showAndWait();        }
    }
}
