package com.example.doctorappointments.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class FirstInterfaceController {

    @FXML
    public ImageView imageView1;

    @FXML
    public ImageView imageView2;

    @FXML
    public ImageView imageView3;

    @FXML
    public ImageView imageView4;

    @FXML
    public ImageView imageView5;

    @FXML
    public ImageView imageView6;







    @FXML
    public void initialize() {
        Image image1 = new Image(getClass().getResource("/images/heartbeats_logo.png").toExternalForm());
        imageView1.setImage(image1);
        Image image2 = new Image(getClass().getResource("/images/doctoricon.png").toExternalForm());
        imageView2.setImage(image2);
        Image image3 = new Image(getClass().getResource("/images/listAppointments1.png").toExternalForm());
        imageView3.setImage(image3);
        Image image4 = new Image(getClass().getResource("/images/calendarimage.png").toExternalForm());
        imageView4.setImage(image4);
        Image image5 = new Image(getClass().getResource("/images/bookAppointment1.png").toExternalForm());
        imageView5.setImage(image5);
        Image image6 = new Image(getClass().getResource("/images/doctor2.png").toExternalForm());
        imageView6.setImage(image6);
    }

    @FXML
    public void handleAppointmentListClick() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/doctorappointments/appointment-list.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Appointments List");
            stage.setScene(new Scene(root, 1100, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAvailabilityClick() throws IOException {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/doctorappointments/Availability.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Manage Availability");
            stage.setScene(new Scene(root, 1100, 700));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void handleAppointmentCreationClick() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/doctorappointments/booking.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Appointments List");
            stage.setScene(new Scene(root, 1100, 700));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void handleDoctorsClick() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/doctorappointments/doctors-list.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Appointments List");
            stage.setScene(new Scene(root, 1100, 700));

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
