module com.javafx.addressconverter {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires org.apache.poi.ooxml;
    requires com.fasterxml.jackson.databind;
    requires java.net.http;
    requires io.github.cdimascio.dotenv.java;
    requires java.logging;

    opens com.javafx.addressconverter to javafx.fxml;
    exports com.javafx.addressconverter;
    exports com.javafx.addressconverter.controller;
    opens com.javafx.addressconverter.controller to javafx.fxml;
}