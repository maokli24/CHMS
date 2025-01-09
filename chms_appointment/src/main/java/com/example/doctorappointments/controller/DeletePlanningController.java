package com.example.doctorappointments.controller;

import com.example.doctorappointments.model.DoctorDTO;
import com.example.doctorappointments.service.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.example.doctorappointments.service.DoctorDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;

public class DeletePlanningController {

    @FXML
    public ComboBox<String> doctorComboBox; // Pour choisir le médecin

    @FXML
    public DatePicker datePicker; // Pour choisir la date

    @FXML
    public ListView<String> slotsListView; // Liste des créneaux disponibles

    @FXML
    public Button deleteButton; // Bouton de suppression

    @FXML
    public Button loadSlotsButton; // Bouton pour charger les créneaux

    public ObservableList<String> availableSlots = FXCollections.observableArrayList();

    public Map<String, Integer> doctorMap = new HashMap<>(); // Associe les noms aux IDs

    public void initialize() {
        try {
            DoctorDAO doctorDAO = new DoctorDAO();
            List<DoctorDTO> doctors = doctorDAO.getAllDoctors();

            for (DoctorDTO doctor : doctors) {
                String displayName = "Dr " + doctor.getName() + " - " + doctor.getSpeciality();
                doctorComboBox.getItems().add(displayName);
                doctorMap.put(displayName, doctor.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean isValidTimeSlot(String timeSlot) {
        return timeSlot.matches("^\\d{2}:\\d{2}$") || timeSlot.matches("^\\d{2}:\\d{2}-\\d{2}:\\d{2}$");
    }

    @FXML
    public void editSelectedSlot() {
        String selectedSlot = slotsListView.getSelectionModel().getSelectedItem();

        if (selectedSlot == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un créneau à modifier.");
            return;
        }

        // Open a dialog to input the new slot
        TextInputDialog dialog = new TextInputDialog(selectedSlot);
        dialog.setTitle("Modifier le créneau");
        dialog.setHeaderText("Modifier le créneau sélectionné");
        dialog.setContentText("Nouveau créneau (HH:mm):"); // Adjusted to HH:mm

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty() || result.get().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun créneau n'a été saisi.");
            return;
        }

        String newSlot = result.get();

        // Validate the new slot format (HH:mm)
        if (!isValidTimeSlot(newSlot)) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez entrer un créneau valide au format HH:mm.");
            return;
        }

        String selectedDoctor = doctorComboBox.getSelectionModel().getSelectedItem();
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDoctor == null || selectedDate == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez sélectionner un médecin et une date.");
            return;
        }

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Split the new slot into start time (since there's no end time in HH:mm format)
            String newStartTime = newSlot.trim();

            // Split the selected slot into start time (same as above)
            String oldStartTime = selectedSlot.trim();

            // Calculate the end time (one hour after the start time)
            String newEndTime = calculateEndTime(newStartTime);

            // Update the slot in the database
            String query = """
        UPDATE planning
        SET Date_Start = ?, Date_Fin = ?
        WHERE IDDoctor = ? AND Date = ? AND Date_Start = ?
        """;

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, newStartTime);
            statement.setString(2, newEndTime); // Use the calculated end time
            statement.setInt(3, doctorMap.get(selectedDoctor)); // Get doctor ID from the map
            statement.setDate(4, java.sql.Date.valueOf(selectedDate.toString()));
            statement.setString(5, oldStartTime);

            System.out.println("Executing update query with parameters:");
            System.out.println("New Start Time: " + newStartTime);
            System.out.println("New End Time: " + newEndTime);
            System.out.println("Doctor ID: " + doctorMap.get(selectedDoctor));
            System.out.println("Selected Date: " + selectedDate.toString());
            System.out.println("Old Start Time: " + oldStartTime);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated > 0) {
                // Update the slot in the ListView
                availableSlots.set(availableSlots.indexOf(selectedSlot), newSlot);
                showAlert(Alert.AlertType.INFORMATION, "Succès", "Le créneau a été modifié avec succès !");
            } else {
                showAlert(Alert.AlertType.ERROR, "Erreur", "La modification du créneau a échoué. Veuillez vérifier les données.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Une erreur est survenue lors de la modification du créneau.");
        }
    }

    /**
     * Calculate the end time for a given start time (1 hour after the start).
     * @param startTime Start time in HH:mm format.
     * @return Calculated end time in HH:mm format.
     */
    public String calculateEndTime(String startTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime start = LocalTime.parse(startTime, formatter);
        LocalTime end = start.plusHours(1); // Adding 1 hour to the start time
        return end.format(formatter);
    }


    // Charger les créneaux disponibles selon le médecin et la date
    @FXML
    public void loadAvailableSlots() {
        String selectedDoctor = doctorComboBox.getSelectionModel().getSelectedItem();
        String selectedDate = (datePicker.getValue() != null) ? datePicker.getValue().toString() : null;

        if (selectedDoctor == null || selectedDate == null) {
            slotsListView.setItems(FXCollections.observableArrayList("Veuillez sélectionner un médecin et une date."));
            deleteButton.setDisable(true);
            return;
        }

        // Récupérer l'ID du médecin depuis le doctorMap
        Integer doctorId = doctorMap.get(selectedDoctor);

        if (doctorId == null) {
            // Si l'ID du médecin n'est pas trouvé, afficher un message d'erreur
            showAlert(Alert.AlertType.INFORMATION, "Erreur", "Médecin introuvable.");
            return;
        }

        availableSlots.clear(); // Vider la liste des créneaux précédents

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Requête SQL mise à jour pour utiliser l'ID du médecin
            String query = """
           SELECT p.Date_Start
           FROM planning p
           WHERE p.IDDoctor = ? AND p.Date = ? AND p.Availability = 1
        """;

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, doctorId); // Passer l'ID du médecin
            statement.setString(2, selectedDate); // Passer la date sélectionnée

            ResultSet resultSet = statement.executeQuery();

            // Ajouter les créneaux à la liste
            while (resultSet.next()) {
                String timeString = resultSet.getString("Date_Start");
                LocalTime time = LocalTime.parse(timeString); // Parse the time
                String formattedTime = time.format(DateTimeFormatter.ofPattern("HH:mm")); // Format it to HH:mm
                availableSlots.add(formattedTime);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Si aucun créneau n'est disponible, afficher un message
        if (availableSlots.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "Aucun créneau disponible", "Il n'y a pas de créneau disponible pour ce médecin et cette date.");
        } else {
            slotsListView.setItems(availableSlots);
        }

        // Activer ou désactiver le bouton de suppression en fonction des créneaux disponibles
        deleteButton.setDisable(availableSlots.isEmpty());
    }


    // Supprimer le ou les créneaux sélectionnés
    @FXML
    public void deleteSelectedSlots() {
        ObservableList<String> selectedSlots = slotsListView.getSelectionModel().getSelectedItems();

        if (selectedSlots.isEmpty()) {
            return; // Si aucun créneau n'est sélectionné, ne rien faire
        }

        String selectedDoctor = doctorComboBox.getSelectionModel().getSelectedItem();
        String selectedDate = (datePicker.getValue() != null) ? datePicker.getValue().toString() : null;

        try (Connection connection = DatabaseConnection.getConnection()) {
            // Supprimer les créneaux sélectionnés
            String query = """
                DELETE FROM planning
                WHERE IDDoctor = (SELECT IDDoctor FROM doctor WHERE Nom = ?) AND Date = ? AND Date_Start = ?
            """;

            PreparedStatement statement = connection.prepareStatement(query);

            for (String timeSlot : selectedSlots) {
                statement.setString(1, selectedDoctor);
                statement.setString(2, selectedDate);
                statement.setString(3, timeSlot);
                statement.executeUpdate();
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Mettre à jour la liste après suppression
        availableSlots.removeAll(selectedSlots);
        deleteButton.setDisable(availableSlots.isEmpty()); // Désactiver le bouton si plus de créneaux

        showAlert(Alert.AlertType.INFORMATION, "Suppression réussie", "Les créneaux sélectionnés ont été supprimés avec succès.");
    }

    // Méthode pour afficher une alerte
    public void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);  // Use the passed alert type
        alert.setTitle(title);
        alert.setHeaderText(null); // Optional, set if you want a header
        alert.setContentText(message);
        alert.showAndWait();
    }
}
