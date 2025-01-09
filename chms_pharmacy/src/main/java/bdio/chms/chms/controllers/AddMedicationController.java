package bdio.chms.chms.controllers;

import bdio.chms.chms.models.Medicament;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import bdio.chms.chms.dao.MedicationService;
import javafx.scene.control.Alert;

import java.util.UUID;

public class AddMedicationController {

    @FXML
    public TextField nameTextField;
    @FXML
    public TextField descriptionTextField;
    @FXML
    public TextField expirationTimeTextField;
    @FXML
    public TextField priceTextField;
    @FXML
    public TextField typeTextField;
    @FXML
    public TextField quantiteTextField; // Nouveau champ pour la quantité

    public MedicationService medicationService;

    public AddMedicationController() {
        this.medicationService = new MedicationService();
    }

    @FXML
    public void initialize() {
        // Code d'initialisation, si nécessaire
    }

    @FXML
    public void handleSaveMedication() {
        if (!validateInputFields()) return;

        String id = UUID.randomUUID().toString();
        Medicament medicament = new Medicament(
                id,
                nameTextField.getText(),
                Double.parseDouble(priceTextField.getText()),
                descriptionTextField.getText(),
                expirationTimeTextField.getText(),
                typeTextField.getText(),
                Integer.parseInt(quantiteTextField.getText()) // Gestion de la quantité
        );
        medicationService.addMedication(medicament);
        clearFormFields();
    }

    public boolean validateInputFields() {
        if (nameTextField.getText().isEmpty() ||
                descriptionTextField.getText().isEmpty() ||
                expirationTimeTextField.getText().isEmpty() ||
                priceTextField.getText().isEmpty() ||
                typeTextField.getText().isEmpty() ||
                quantiteTextField.getText().isEmpty()) { // Vérification du champ quantité
            showErrorMessage("Input Error", "All fields must be filled out.");
            return false;
        }
        try {
            double price = Double.parseDouble(priceTextField.getText());
            if (price <= 0) {
                showErrorMessage("Input Error", "Price must be positive.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Input Error", "Price must be numeric.");
            return false;
        }
        try {
            int quantite = Integer.parseInt(quantiteTextField.getText());
            if (quantite < 0) {
                showErrorMessage("Input Error", "Quantity cannot be negative.");
                return false;
            }
        } catch (NumberFormatException e) {
            showErrorMessage("Input Error", "Quantity must be numeric.");
            return false;
        }
        return true;
    }

    public void clearFormFields() {
        nameTextField.clear();
        descriptionTextField.clear();
        expirationTimeTextField.clear();
        priceTextField.clear();
        typeTextField.clear();
        quantiteTextField.clear(); // Effacer le champ quantité
    }

    public void showErrorMessage(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
