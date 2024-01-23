module com.realisatie.realisatiesiem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.realisatie.realisatiesiem to javafx.fxml;
    exports com.realisatie.realisatiesiem;
}