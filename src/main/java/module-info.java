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
    exports com.lulakssoft.activitymanagement.adapter.ui;
    opens com.lulakssoft.activitymanagement.adapter.ui to javafx.fxml;
    exports com.lulakssoft.activitymanagement.domain.model.activity;
    opens com.lulakssoft.activitymanagement.domain.model.activity to javafx.fxml;
    exports com.lulakssoft.activitymanagement.domain.model.project;
    opens com.lulakssoft.activitymanagement.domain.model.project to javafx.fxml;
    exports com.lulakssoft.activitymanagement.domain.model.user;
    opens com.lulakssoft.activitymanagement.domain.model.user to javafx.fxml;
    exports com.lulakssoft.activitymanagement.application.service;
    opens com.lulakssoft.activitymanagement.application.service to javafx.fxml;
}