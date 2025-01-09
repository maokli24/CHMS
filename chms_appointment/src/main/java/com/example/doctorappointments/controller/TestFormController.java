package com.example.doctorappointments.controller;

import com.example.doctorappointments.model.AppointmentDetails;
import com.example.doctorappointments.model.Test;
import com.example.doctorappointments.service.AppointmentService;
import com.example.doctorappointments.service.OrdonnanceService;
import com.example.doctorappointments.service.TestService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class TestFormController {
    public int appointmentID;

    @FXML
    public Label title;

    @FXML
    public ComboBox<String> testsComboBox;

    @FXML
    public TableView<String> testsTableView;

    @FXML
    public TableColumn<String, String> testColumn;

    @FXML
    public DatePicker date_test;

    @FXML
    public Label label_doctor;

    @FXML
    public Label label_patient;

    public AppointmentDetails appointment = null;

    public final TestService testService  = new TestService();
    public Map<String, Integer> testMap = new HashMap<>();

    @FXML
    public void initialize() {
        loadtests();

        testColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    }

    public void loadtests() {
        ObservableList<Test> tests = testService.getAllTests();
        ObservableList<String> testNames = FXCollections.observableArrayList();

        for (Test test : tests) {
            testNames.add(test.getName());
            testMap.put(test.getName(), test.getIDTest());
        }
        testsComboBox.setItems(testNames);




        /* ObservableList<String> testList = FXCollections.observableArrayList("Blood Test", "X-Ray", "MRI", "CT Scan", "Ultrasound", "ECG", "Echocardiogram", "Biopsy", "Allergy Test");
        testsComboBox.setItems(testList); */
    }

    public void setAppointmentID(int appointmentID) {
        System.out.println("Appointment ID: " + appointmentID);
        this.appointmentID = appointmentID;
        title.setText("Add New Prescription Test : " + appointmentID);

        appointment = AppointmentService.getAppointmentById(appointmentID);


        if (appointment != null) {
            label_doctor.setText("Doctor: " + appointment.getDoctorFullName());
            label_patient.setText("Patient: " + appointment.getPatientFullName());
        }
    }

    @FXML
    public void handleAddTestAction() {
        String selectedTest = testsComboBox.getValue();
        System.out.println(selectedTest);
        if (selectedTest != null && !testsTableView.getItems().contains(selectedTest)) {
            testsTableView.getItems().add(selectedTest);
        }
    }

    @FXML
    public void handleSubmitButtonAction() {
        if (date_test.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "No date selected.");
            return;
        }

        ObservableList<String> selectedTests = testsTableView.getItems();
        if (selectedTests.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "No tests selected.");
            return;
        }

        // Collect all test IDs
        ObservableList<Integer> testIDs = FXCollections.observableArrayList();
        for (String testName : selectedTests) {
            Integer medicationID = testMap.get(testName);
            if (medicationID != null) {
                testIDs.add(medicationID);
            } else {
                showAlert(Alert.AlertType.WARNING, "Test ID not found for: " + testName);
                return; // Exit if any ID is missing
            }
        }

        System.out.println("Selected Tests: " + selectedTests);
        System.out.println("Selected Test IDs: " + testIDs);
        System.out.println("Test Date: " + date_test.getValue());
        Boolean done = OrdonnanceService.insertOrdonnanceTest(appointment.getIDDoctor(), java.sql.Timestamp.valueOf(date_test.getValue().atStartOfDay()), appointment.getIDPatient(), testIDs);

        if (done) {
            showAlert(Alert.AlertType.INFORMATION, "Prescription added successfully!");
            ((Stage) title.getScene().getWindow()).close();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error in inserting the prescription");
        }
    }

    public void showAlert(Alert.AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
