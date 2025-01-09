package bdio.chms.chms.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import bdio.chms.chms.models.Medicament;
import bdio.chms.chms.dao.MedicationService;
import javafx.stage.Stage;

import java.io.IOException;

public class FournisseurController {

    @FXML
    public TableView<Medicament> medicamentTable;

    @FXML
    public TableColumn<Medicament, String> nomColumn;

    @FXML
    public TableColumn<Medicament, Integer> qtyColumn;

    @FXML
    public TableColumn<Medicament, Double> prixColumn;

    @FXML
    public Button increaseButton;

    public MedicationService medicationService;

    @FXML
    public void initialize() {
        System.out.println("Initialisation du contrôleur...");
        medicationService = new MedicationService();  // Initialize the MedicationService

        nomColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().quantiteProperty().asObject());
        prixColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());

        chargerMedicaments();
    }

    public void chargerMedicaments() {
        if (medicamentTable != null) {
            // Fetch the list of medicaments from the service
            ObservableList<Medicament> medicaments = FXCollections.observableArrayList(medicationService.getAllMedications());
            medicamentTable.setItems(medicaments);
        } else {
            System.err.println("medicamentTable is null!");
        }
    }

    // Method to increase quantity
    public void augmenterQuantite(ActionEvent event) {
        Medicament selectedMedicament = medicamentTable.getSelectionModel().getSelectedItem();

        if (selectedMedicament != null) {
            int nouvelleQuantite = selectedMedicament.getQuantite() + 10; // Increase quantity by 10
            medicationService.updateQuantite(selectedMedicament.getId(), nouvelleQuantite); // Update quantity in database

            // Update the quantity in the table and refresh
            selectedMedicament.setQuantite(nouvelleQuantite);
            medicamentTable.refresh();
        }
    }

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
    @FXML
    public void  goToLogin(ActionEvent event) {
        loadView("/bdio/chms/chms/fxml/login-view.fxml", event);
    }
}
