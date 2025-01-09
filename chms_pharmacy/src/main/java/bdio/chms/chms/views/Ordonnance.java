package bdio.chms.chms.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Ordonnance extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/bdio/chms/chms/fxml/Ordonnance.fxml"));
        Parent root = (Parent)fxmlLoader.load();
        stage.setScene(new Scene(root,1200, 800));
        Image icon = new Image(getClass().getResourceAsStream("/bdio/chms/chms/images/heartbeats_logo.png"));
        stage.getIcons().add(icon);
        stage.setTitle("Prescriptions");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}