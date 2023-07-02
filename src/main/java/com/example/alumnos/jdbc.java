package jpa;

import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.sql.*;


public class jdbc {
        Connection c;
        String URL = "jdbc:mysql://localhost:3306/jdbc";
        String user = "root";
        String password = "";
        public void connect() {

                try {
                        Class.forName("com.mysql.jdbc.Driver");
                        c = (Connection) DriverManager.getConnection(URL,user,password);
                        System.out.println("Conexion realizada ............");

                } catch (ClassNotFoundException | SQLException e) {
                        e.printStackTrace();
                        System.out.println("Conexion no realizada ..............");


                }
        }
}