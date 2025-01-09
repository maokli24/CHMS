module bdio.chms.chms {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;
    requires java.desktop;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.javafx;
    requires mysql.connector.j;


    exports bdio.chms.chms.models;
    opens bdio.chms.chms.models to javafx.fxml;
    exports bdio.chms.chms.dao;
    opens bdio.chms.chms.dao to javafx.fxml;
    exports bdio.chms.chms.controllers;
    opens bdio.chms.chms.controllers to javafx.fxml;
    exports bdio.chms.chms.views;
    opens bdio.chms.chms.views to javafx.fxml;
}