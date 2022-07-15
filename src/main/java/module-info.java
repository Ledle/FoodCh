module com.foodch.foodch {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.foodch to javafx.fxml;
    exports com.foodch;
}