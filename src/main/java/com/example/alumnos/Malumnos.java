package com.example.alumnos;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Malumnos extends Application {

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
    private void generarReporte() {
        int codigo = Integer.parseInt(codigoTextField.getText());
        String nombre = nombreTextField.getText();
        String apellido = apellidoTextField.getText();
        String genero = generoChoiceBox.getValue();
        String modalidad = modalidadChoiceBox.getValue();

        double monto = calcularMonto(modalidad);
        int duracion = calcularDuracion(modalidad);

        montoLabel.setText(String.valueOf(monto));
        duracionLabel.setText(String.valueOf(duracion));

        List<CAlumnos> alumnos = new ArrayList<>();
        CAlumnos alumno = new CAlumnos(codigo, nombre, apellido, genero, modalidad);
        alumnos.add(alumno);

        try {
            putAlumnos(alumnos);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void limpiarCampos() {
        codigoTextField.clear();
        nombreTextField.clear();
        apellidoTextField.clear();
        generoChoiceBox.setValue(null);
        modalidadChoiceBox.setValue(null);
        montoLabel.setText("");
        duracionLabel.setText("");
    }

    public List<CAlumnos> getAlumnos() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/alumnos";
        String user = "root";
        String pass = "";

        List<CAlumnos> alumnos = new ArrayList<>();

        try (Connection conexion = DriverManager.getConnection(url, user, pass)) {
            Statement st = conexion.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM alumnos");

            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String genero = rs.getString("genero");
                String modalidad = rs.getString("modalidad");

                CAlumnos alumno = new CAlumnos(codigo, nombre, apellido, genero, modalidad);
                alumnos.add(alumno);
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new SQLException("Error al obtener los alumnos de la base de datos", e);
        }

        return alumnos;
    }

    public void putAlumnos(List<CAlumnos> alumnos) throws SQLException {
        String url = "jdbc:mysql://localhost:3306/alumnos";
        String user = "root";
        String pass = "";

        try (Connection conexion = DriverManager.getConnection(url, user, pass)) {
            Statement stmnt = conexion.createStatement();
            stmnt.executeUpdate("DELETE FROM alumnos");
            stmnt.close();

            String query = "INSERT INTO alumnos (codigo, nombre, apellido, genero, modalidad) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conexion.prepareStatement(query);

            for (CAlumnos alumno : alumnos) {
                ps.setInt(1, alumno.getCodigo());
                ps.setString(2, alumno.getNombre());
                ps.setString(3, alumno.getApellido());
                ps.setString(4, alumno.getGenero());
                ps.setString(5, alumno.getModalidad());
                ps.executeUpdate();
            }

            ps.close();
            System.out.println("Datos guardados en la base de datos");
        } catch (SQLException e) {
            throw new SQLException("Error al guardar los alumnos en la base de datos", e);
        }
    }

    @FXML
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/alumnos/alumnos.fxml"));
        AnchorPane root = loader.load();
        Scene scene = new Scene(root, 600, 582);
        primaryStage.setScene(scene);
        primaryStage.show();

        codigoTextField = (TextField) loader.getNamespace().get("codigoTextField");
        nombreTextField = (TextField) loader.getNamespace().get("nombreTextField");
        apellidoTextField = (TextField) loader.getNamespace().get("apellidoTextField");
        generoChoiceBox = (ChoiceBox<String>) loader.getNamespace().get("generoChoiceBox");
        modalidadChoiceBox = (ChoiceBox<String>) loader.getNamespace().get("modalidadChoiceBox");

        generoChoiceBox.getItems().addAll("Masculino", "Femenino", "Otro");
        modalidadChoiceBox.getItems().addAll("Presencial", "Semipresencial", "Virtual");
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
