package bdio.chms.chms.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Medicament extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bdio/chms/chms/fxml/Medicament.fxml"));
        BorderPane xA = fxmlLoader.load();

        Scene scene = new Scene(xA, 1200, 800);
        scene.getStylesheets().add(getClass().getResource("/bdio/chms/chms/fxml/styles.css").toExternalForm());

        primaryStage.setScene(scene);
        primaryStage.setTitle("Pharmacy Management System");
        primaryStage.show();


    }

    public static void main(String[] args) {
        launch(args);
    }
}