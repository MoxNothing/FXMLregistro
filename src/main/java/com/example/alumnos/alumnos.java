package com.example.alumnos;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class alumnos extends Application {

    @FXML
    private TextField codigoTextField;

    @FXML
    private TextField nombreTextField;

    @FXML
    private TextField apellidoTextField;

    @FXML
    private ChoiceBox<String> generoChoiceBox;

    @FXML
    private ChoiceBox<String> modalidadChoiceBox;

    @FXML
    private Label montoLabel;

    @FXML
    private Label duracionLabel;

    @FXML
    private void generarReporte(ActionEvent event) {

        String codigo = codigoTextField.getText();
        String nombre = nombreTextField.getText();
        String apellido = apellidoTextField.getText();
        String genero = generoChoiceBox.getValue();
        String modalidad = modalidadChoiceBox.getValue();

        double monto = calcularMonto(modalidad);
        int duracion = calcularDuracion(modalidad);

        montoLabel.setText(String.valueOf(monto));
        duracionLabel.setText(String.valueOf(duracion));
    }

    @FXML
    private void limpiarCampos(ActionEvent event) {

        codigoTextField.clear();
        nombreTextField.clear();
        apellidoTextField.clear();
        generoChoiceBox.setValue(null);
        modalidadChoiceBox.setValue(null);
        montoLabel.setText("");
        duracionLabel.setText("");
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/alumnos/alumnos.fxml"));
            AnchorPane root = loader.load();
            Scene scene = new Scene(root, 600, 582);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private double calcularMonto(String modalidad) {
        if (modalidad.equalsIgnoreCase("presencial")) {
            return 583.0;
        } else if (modalidad.equalsIgnoreCase("semipresencial")) {
            return 500.0;
        } else if (modalidad.equalsIgnoreCase("virtual")) {
            return 480.0;
        } else {
            return 0.0;
        }
    }

    private int calcularDuracion(String modalidad) {
        if (modalidad.equalsIgnoreCase("presencial")) {
            return 4;
        } else if (modalidad.equalsIgnoreCase("semipresencial")) {
            return 4;
        } else if (modalidad.equalsIgnoreCase("virtual")) {
            return 2;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
