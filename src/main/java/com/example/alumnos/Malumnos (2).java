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
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

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
            generarInforme();
        } catch (SQLException | JRException e) {
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
            PreparedStatement pst = conexion.prepareStatement(query);

            for (CAlumnos alumno : alumnos) {
                pst.setInt(1, alumno.getCodigo());
                pst.setString(2, alumno.getNombre());
                pst.setString(3, alumno.getApellido());
                pst.setString(4, alumno.getGenero());
                pst.setString(5, alumno.getModalidad());
                pst.executeUpdate();
            }

            pst.close();
        } catch (SQLException e) {
            throw new SQLException("Error al insertar los alumnos en la base de datos", e);
        }
    }

    public double calcularMonto(String modalidad) {
        double monto = 0;

        switch (modalidad) {
            case "presencial":
                monto = 1000.00;
                break;
            case "semipresencial":
                monto = 800.00;
                break;
            case "virtual":
                monto = 600.00;
                break;
        }

        return monto;
    }

    public int calcularDuracion(String modalidad) {
        int duracion = 0;

        switch (modalidad) {
            case "presencial":
                duracion = 12;
                break;
            case "semipresencial":
                duracion = 9;
                break;
            case "virtual":
                duracion = 6;
                break;
        }

        return duracion;
    }

    public void generarInforme() throws JRException {
        try {
            String jrxmlFile = "C:\\Users\\ASUS\\JaspersoftWorkspace\\MyReports\\registro.jrxml"; // la ruta del archivo .jrxml
            String jasperFile = "C:\\Users\\ASUS\\JaspersoftWorkspace\\MyReports\\registro.jasper"; // la ruta del archivo .jasper

            // Inicia el archivo .jrxml a .jasper
            JasperCompileManager.compileReportToFile(jrxmlFile, jasperFile);

            // inicia el archivo .jasper
            JasperReport jasperReport = (JasperReport) JRLoader.loadObjectFromFile(jasperFile);

            // crear el objeto JasperPrint
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());

            // Genera el informe
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            throw new JRException("Error al generar el informe", e);
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("alumnos.fxml"));//genera el archivo fxml
        AnchorPane root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

