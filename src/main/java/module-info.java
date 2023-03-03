module com.example.test {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;
    opens com.example.test to javafx.fxml;
    exports com.example.test;
    exports com.example.test.data.model;
    opens com.example.test.data.model to javafx.fxml;
}