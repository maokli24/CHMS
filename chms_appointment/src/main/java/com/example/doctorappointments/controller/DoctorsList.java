package com.example.doctorappointments.controller;

import com.example.doctorappointments.model.Doctor;
import com.example.doctorappointments.model.Speciality;
import com.example.doctorappointments.service.DoctorService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class DoctorsList {

    @FXML
    public TableView<Doctor> doctorTableView;

    @FXML
    public TableColumn<Doctor, String> doctorNameTableColumn;

    @FXML
    public TableColumn<Doctor, String> specialityTableColumn;

    @FXML
    public TableColumn<Doctor, String> telTableColumn;

    @FXML
    public TableColumn<Doctor, String> adresseTableColumn;
    @FXML
    public Button filterButton;
    @FXML
    public TextField searchField; // Bind this to the search functionality

    @FXML
    public TextField doctorNameField;

    @FXML
    public TextField doctorSpecialityField;

    @FXML
    public TextField doctorPhoneField;

    @FXML
    public TextField doctorAddressField;

    public ObservableList<Doctor> doctors = FXCollections.observableArrayList();
    public DoctorService doctorService=new DoctorService();
    @FXML
    public TableColumn<Doctor, Void> ActionsTableColumn;

    @FXML
    public void initialize() {
        // Fetch doctors from the service
        doctors.addAll(doctorService.getAllDoctors());  // Assuming this method returns all doctors

        // Setting up the TableView columns
        doctorNameTableColumn.setCellValueFactory(cellData -> {
            Doctor doctor = cellData.getValue();
            return new javafx.beans.property.SimpleStringProperty(doctor.getPrenom() + " " + doctor.getNom());
        });

        specialityTableColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getSpecialityName()));  // Show the speciality name
        telTableColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTel()));
        adresseTableColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getAdresse()));


        // Dynamically set column widths (optional)
        doctorNameTableColumn.prefWidthProperty().bind(doctorTableView.widthProperty().multiply(0.25));
        specialityTableColumn.prefWidthProperty().bind(doctorTableView.widthProperty().multiply(0.20));
        telTableColumn.prefWidthProperty().bind(doctorTableView.widthProperty().multiply(0.25));
        adresseTableColumn.prefWidthProperty().bind(doctorTableView.widthProperty().multiply(0.30));

        // Populate the TableView with the doctor data


        filterButton.setOnMouseClicked(this::onFilterButtonClicked);

        ActionsTableColumn = new TableColumn<>("Actions");
        doctorTableView.getColumns().add(ActionsTableColumn);


        ActionsTableColumn.setCellFactory(param -> new TableCell<Doctor, Void>() {
            /*public final Button updateDoctorButton = new Button("Update");
            public final Button deleteDoctorButton = new Button("Delete");
            public final HBox buttons = new HBox(updateDoctorButton, deleteDoctorButton);*/
            public final Button updateDoctorButton = new Button("Update");
            public final Button deleteDoctorButton = new Button("Delete");
            public final HBox buttons = new HBox(10, updateDoctorButton, deleteDoctorButton); // Add spacing between buttons



            public void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getIndex() < 0 || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    Doctor doctor = getTableView().getItems().get(getIndex());
                    updateDoctorButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"); // Green update button
                    deleteDoctorButton.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;"); // Red delete button

                    updateDoctorButton.setOnAction(event -> handleDoctorUpdate(doctor));
                    deleteDoctorButton.setOnAction(event -> handleDoctorDelete(doctor));
                    setGraphic(buttons);
                }
            }
        });



        doctorTableView.setItems(doctors);
    }

    public void handleDoctorDelete(Doctor doctor) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Doctor");
        alert.setHeaderText("This will delete the doctor and all associated appointments. Proceed?");
        alert.setContentText("Doctor: " + doctor.getPrenom() + " " + doctor.getNom());

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean res = DoctorService.deleteDoctor(doctor.getIdDoctor());
                if (res) {
                    refreshDoctorData();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText("Failed to delete doctor");
                    errorAlert.setContentText("An error occurred while trying to delete the doctor.");
                    errorAlert.showAndWait();
                }
            }
        });
    }

    public void handleDoctorUpdate(Doctor doctor) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/doctorappointments/doctor-update-form.fxml"));
            Parent root = fxmlLoader.load();

            // Get the controller for the update form
            DoctorUpdateFormController controller = fxmlLoader.getController();
            controller.setDoctorID(doctor.getIdDoctor());

            // Pass the DoctorsList controller to the update form
            controller.setDoctorsListController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1100, 700));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    // Handle filter button click
    public void onFilterButtonClicked(MouseEvent event) {
        // Open a dialog or dropdown with all available specialties
        List<Speciality> specialties = DoctorService.getAllSpecialities();
        ChoiceDialog<Speciality> dialog = new ChoiceDialog<>(null, specialties);
        dialog.setTitle("Choose a Specialty");
        dialog.setHeaderText("Select the specialty to filter doctors by");
        dialog.setContentText("Specialty:");


        dialog.showAndWait().ifPresent(selectedSpeciality -> {
            filterDoctorsBySpecialty(selectedSpeciality.getNomSpeciality());
        });
    }


    public void filterDoctorsBySpecialty(String specialtyName) {
        ObservableList<Doctor> filteredDoctors = FXCollections.observableArrayList();

        for (Doctor doctor : doctors) {
            if (doctor.getSpecialityName().equalsIgnoreCase(specialtyName)) {
                filteredDoctors.add(doctor);
            }
        }

        doctorTableView.setItems(filteredDoctors);  // Display filtered list
    }
    public void handleRowSelection(MouseEvent event) {
        Doctor selectedDoctor = doctorTableView.getSelectionModel().getSelectedItem();
        if (selectedDoctor != null) {
            // Display selected doctor details in text fields
            doctorNameField.setText(selectedDoctor.getPrenom() + " " + selectedDoctor.getNom());
            doctorSpecialityField.setText(String.valueOf(selectedDoctor.getIdSpeciality())); // You can map this to the specialty name if needed
            doctorPhoneField.setText(selectedDoctor.getTel());
            doctorAddressField.setText(selectedDoctor.getAdresse());
        }
    }

    public void refreshDoctorData() {
        // Clear the current list and fetch updated data
        doctors.clear();
        doctors.addAll(doctorService.getAllDoctors()); // Fetch updated list of doctors
        doctorTableView.refresh(); // Refresh TableView to reflect new data
    }

    public void handleNewDoctor(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/doctorappointments/doctor-form.fxml"));
            Parent root = fxmlLoader.load();

            // Get the controller for the update form
            DoctorFormController controller = fxmlLoader.getController();

            // Pass the DoctorsList controller to the update form
            controller.setDoctorsListController(this);
            Stage stage = new Stage();
            stage.setScene(new Scene(root, 1100, 700));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}