module com.lulakssoft.activitymanagement {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.zaxxer.hikari;
    requires java.sql;
    requires org.slf4j;

    opens com.lulakssoft.activitymanagement to javafx.fxml;
    exports com.lulakssoft.activitymanagement;
}