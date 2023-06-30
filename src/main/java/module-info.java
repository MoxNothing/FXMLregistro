module com.example.alumnos {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                        
    opens com.example.alumnos to javafx.fxml;
    exports com.example.alumnos;
}