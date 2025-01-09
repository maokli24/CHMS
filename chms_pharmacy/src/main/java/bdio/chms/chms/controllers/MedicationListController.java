package bdio.chms.chms.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import bdio.chms.chms.models.Medicament;
import bdio.chms.chms.dao.MedicationService;

public class MedicationListController {

    @FXML
    public TableView<Medicament> medicationTable;

    @FXML
    public TableColumn<Medicament, String> idColumn;

    @FXML
    public TableColumn<Medicament, String> nameColumn;

    @FXML
    public TableColumn<Medicament, String> descriptionColumn;

    @FXML
    public TableColumn<Medicament, String> expirationTimeColumn;

    @FXML
    public TableColumn<Medicament, Double> priceColumn;

    @FXML
    public TableColumn<Medicament, String> typeColumn;

    public MedicationService medicationService;

    @FXML
    public void initialize() {
        medicationService = new MedicationService();

        // Set the cell value factory for each column
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        expirationTimeColumn.setCellValueFactory(cellData -> cellData.getValue().expirationTimeProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());

        // Load medications into the table
        medicationTable.getItems().setAll(medicationService.getAllMedications());
    }
}
