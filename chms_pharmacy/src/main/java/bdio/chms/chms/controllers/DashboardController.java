package bdio.chms.chms.controllers;



import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.event.ActionEvent;

import javafx.scene.Node;





public class DashboardController {


    @FXML
    public Node sidebar;

    public Stage stage;
    public Scene scene;
    public Parent root;

    // Method to load views dynamically into the dashboard content area
    public void loadView(String fxmlFile, ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            root = loader.load();

            // Get the current stage and set the new scene
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);

            // Manage sidebar visibility based on the view being loaded
            manageSidebarVisibility(fxmlFile);

            // Set the scene to the stage and show it
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading view: " + fxmlFile);
            e.printStackTrace();
        }
    }


    public void manageSidebarVisibility(String fxmlFile) {
        if (sidebar != null) {
            boolean isSidebarVisible = fxmlFile.equals("/bdio/chms/chms/fxml/dashboard.fxml") ||
                    fxmlFile.equals("/bdio/chms/chms/fxml/Medicament.fxml");
            sidebar.setVisible(isSidebarVisible);
        }
    }

    @FXML
    public void goToProfile(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/profile.fxml", event);
    }

    @FXML
    public void goToDashboard(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/dashboard.fxml", event);
    }

    @FXML
    public void showViewMedication(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/Medicament.fxml", event);
    }

    @FXML
    public void goToOrdonnance(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/Ordonnance.fxml", event);
    }

    @FXML
    public void goToFournisseur(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/fournisseur.fxml", event);
    }
}