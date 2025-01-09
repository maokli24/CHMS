package bdio.chms.chms.controllers;

import bdio.chms.chms.dao.DoctorDao;
import bdio.chms.chms.dao.MedicationService;
import bdio.chms.chms.dao.OrdonnanceDao;
import bdio.chms.chms.dao.PatientDao;
import bdio.chms.chms.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DetailsController {
    @FXML
    public Text patientFirstName;
    @FXML
    public Text patientLastName;
    @FXML
    public Text patientSexLabel;
    @FXML
    public Text patientAgeLabel;
    @FXML
    public Label doctorNameLabel;
    @FXML
    public Label doctorSpecialtyLabel;
    @FXML
    public Label doctorPhoneLabel;
    @FXML
    public Label doctorAddressLabel;
    @FXML
    public ListView<String> medicamentsListView;
    @FXML
    public Button validateButton;

    public Ordonnance ordonnance;
    public DoctorDao doctorDao;
    public OrdonnanceDao ordonnanceDao ;
    public PatientDao patientDao;
    public MedicationService medicationDao;

    public DetailsController() {
        // Initialisation des DAO
        this.doctorDao = new DoctorDao();
        this.ordonnanceDao = new OrdonnanceDao();
        this.patientDao = new PatientDao();
        this.medicationDao = new MedicationService();
    }
    public void setOrdonnance(Ordonnance ordonnance) throws SQLException {
        if (ordonnance.getStatus().equals("Active")) {
            validateButton.setStyle(
                    "-fx-background-color:#5A9BD5FF;-fx-border-radius: 15px;"+
                            "-fx-pref-height: 10px;-fx-text-alignment: center;" +
                            "-fx-pref-width: 100px;-fx-text-fill:white;"+
                            "-fx-font-weight: bold;"
            );
            validateButton.setVisible(true);
        }
        this.ordonnance = ordonnance;
        displayDetails();
    }

    public void displayDetails() throws SQLException {
        Patient patient = null;
        patient = patientDao.getPatientById(ordonnance.getPatient());
        patientFirstName.setText(patient.getPrenom());
        patientLastName.setText(patient.getNom());
        patientSexLabel.setText(patient.getSexe());
        patientAgeLabel.setText(patient.getBirthDate().toString());

        Doctor doctor = null;
        try {
            doctor = doctorDao.getDoctorById(ordonnance.getDoctor());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        doctorNameLabel.setText(doctor.getNom()+" "+doctor.getPrenom());
        Speciality specialty = null;
        try {
            specialty = doctorDao.getSpecialtyByDoctorId(doctor.getIdDoctor());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        doctorSpecialtyLabel.setText(specialty.toString());
        doctorPhoneLabel.setText(doctor.getTel());
        doctorAddressLabel.setText(doctor.getAdresse());

        List<String> medications = new ArrayList<>();
        for (Integer medicationId : ordonnance.getMedicamentIds()) {
            Medicament medication = null;
            try {
                medication = medicationDao.getMedicationById(medicationId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            medications.add(medication.nameProperty().get()); // Get the actual String value
        }


        ObservableList<String> medicationList = FXCollections.observableArrayList(medications);
        medicamentsListView.setItems(medicationList);
    }


    @FXML
    public void validateOrdonnance() {
        ordonnance.setStatus("Completed");
        try {
            ordonnanceDao.updateOrdonnance(ordonnance);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        validateButton.setVisible(false);
    }
}
