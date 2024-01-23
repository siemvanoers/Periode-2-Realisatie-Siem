module com.realisatie.realisatiesiem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.realisatie.realisatiesiem to javafx.fxml;
    exports com.realisatie.realisatiesiem;
}