module com.lulakssoft.activitymanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.lulakssoft.activitymanagement to javafx.fxml;
    exports com.lulakssoft.activitymanagement;
    exports com.lulakssoft.activitymanagement.controller;
    opens com.lulakssoft.activitymanagement.controller to javafx.fxml;
}