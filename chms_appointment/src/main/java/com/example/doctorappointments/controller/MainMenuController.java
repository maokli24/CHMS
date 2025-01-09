package com.example.doctorappointments.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class MainMenuController {

    @FXML
    public BorderPane mainPane;  // Le BorderPane qui contient tout

    @FXML
    public AnchorPane root;

    public Label welcomeLabel;// Zone centrale pour afficher dynamiquement les vues

    // Charger l'interface "Add Work Hour"
    @FXML
    public void loadAddWorkhour() {
        loadUI("addWorkhour.fxml");
    }

    // Charger l'interface "Check Availability"
    @FXML
    public void loadCheckAvailability() {
        loadUI("CheckAvalaibility.fxml");
    }

    // Charger l'interface "Delete Planning"
    @FXML
    public void loadDeletePlanning() {
        loadUI("DeletePlanning.fxml");
    }

    // Charger l'interface "Available Doctors"
    @FXML
    public void loadAvailableDoctors() {
        loadUI("getAvalaibleByDate.fxml");
    }

    // Méthode générique pour charger une interface dans la zone centrale
    public void loadUI(String fxmlFile) {
        try {

            if (welcomeLabel != null) {
                welcomeLabel.setVisible(false);
            }

            // Charger le contenu du fichier FXML dans l'AnchorPane
            Parent view = FXMLLoader.load(getClass().getResource( "/com/example/doctorappointments/" + fxmlFile));
            root.getChildren().clear(); // Nettoyer l'AnchorPane
            root.getChildren().add(view); // Ajouter la nouvelle interface

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
