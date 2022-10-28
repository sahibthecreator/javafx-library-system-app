module com.assignment.javaendassignment {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.assignment.javaendassignment to javafx.fxml;
    exports com.assignment.javaendassignment;
}