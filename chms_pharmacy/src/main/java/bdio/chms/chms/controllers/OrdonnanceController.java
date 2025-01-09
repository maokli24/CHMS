package bdio.chms.chms.controllers;

import bdio.chms.chms.dao.OrdonnanceDao;
import bdio.chms.chms.models.Ordonnance;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class OrdonnanceController {

    @FXML
    public TableView<Ordonnance> ordonnanceTable;

    @FXML
    public TableColumn<Ordonnance, Integer> colIdOrdonnance;

    @FXML
    public TableColumn<Ordonnance, String> colDateOrdonnance;

    @FXML
    public TableColumn<Ordonnance, String> colPatientName;

    @FXML
    public TableColumn<Ordonnance, String> colDoctorName;

    @FXML
    public TableColumn<Ordonnance, String> colStatus;

    public ObservableList<Ordonnance> ordonnanceList;

    @FXML
    public TableColumn<Ordonnance, Void> colDetails;

    @FXML
    public void initialize() {
        colIdOrdonnance.setCellValueFactory(new PropertyValueFactory<>("id"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));

        colStatus.setCellFactory(column -> new TableCell<Ordonnance, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (status != null && !empty) {
                    Label label = new Label(status);
                    if ("Active".equals(status)) {
                        label.setTextFill(Color.WHITE);
                        label.setMaxHeight(15);
                        label.setStyle("-fx-background-color: #FED440; -fx-padding: 5px;-fx-font-weight: bold;-fx-background-radius: 20px; ");
                    } else if ("Completed".equals(status)) {
                        label.setTextFill(Color.WHITE);
                        label.setStyle("-fx-background-color: #59D189; -fx-padding: 5px;-fx-font-weight: bold;-fx-pref-height:12px;-fx-background-radius: 20px; ");
                    } else {
                        label.setTextFill(Color.BLACK);
                        label.setStyle("-fx-background-color: white; -fx-padding: 5px;fx-font-weight: bold;");
                    }
                    setGraphic(label);
                } else {
                    setGraphic(null);
                }
            }
        });


        colDateOrdonnance.setCellValueFactory(new PropertyValueFactory<>("dateCreation"));
        colPatientName.setCellValueFactory(new PropertyValueFactory<>("Patient"));
        colDoctorName.setCellValueFactory(new PropertyValueFactory<>("Doctor"));

        loadOrdonnances();
        addDetailsButtonToTable();

    }

    public void loadOrdonnances() {
        OrdonnanceDao dao = new OrdonnanceDao();
        ordonnanceList = FXCollections.observableArrayList(dao.getAll());
        ordonnanceTable.setItems(ordonnanceList);
    }

    public void addDetailsButtonToTable() {
        TableColumn<Ordonnance, Void> colDetails = new TableColumn<>("Actions");

        colDetails.setCellFactory(param -> new TableCell<>() {
            public final Button btn = new Button();
            public final ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/bdio/chms/chms/images/play.png")));
            {
                icon.setFitHeight(15);
                icon.setFitWidth(15);
                btn.setText("Details");
                btn.setContentDisplay(ContentDisplay.RIGHT);
                btn.setAlignment(Pos.CENTER);
                btn.setPrefWidth(80);
                btn.setMaxHeight(10);
                btn.setStyle(
                        "-fx-background-color:#5A9BD5FF;-fx-text-fill:white;-fx-font-weight:bold;-fx-font-size:11px;"+
                                "-fx-background-radius: 10px;-fx-alignment:center;-fx-pref-height: 10px;"
                );
                btn.setGraphic(icon);
                btn.setOnAction(event -> {
                    Ordonnance ordonnance = getTableView().getItems().get(getIndex());
                    System.out.println(ordonnance);
                    showDetailsWindow(ordonnance);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });

        ordonnanceTable.getColumns().add(colDetails);
    }

    public void showDetailsWindow(Ordonnance ordonnance) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bdio/chms/chms/fxml/Details.fxml"));
            Parent root = loader.load();
            DetailsController detailsController = loader.getController();
            detailsController.setOrdonnance(ordonnance);
            Stage stage = new Stage();
            stage.setTitle(" Prescription Details");
            stage.setScene(new Scene(root,450,500));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

}
