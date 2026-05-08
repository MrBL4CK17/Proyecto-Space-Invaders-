package spaceinvaders;

import java.sql.*;

public class DBHandler {
    private static final String URL = "jdbc:mariadb://localhost:3306/space_invaders";
    private static final String USER = "root"; 
    private static final String PASS = "gamer11271"; 

    public static void guardarPuntaje(String nombre, int puntos, int nivel) {
        new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS)) {
                String query = "INSERT INTO puntajes (nombre, puntos, nivel_alcanzado) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, nombre);
                stmt.setInt(2, puntos);
                stmt.setInt(3, nivel);
                stmt.executeUpdate();
                System.out.println("Puntaje guardado correctamente.");
            } catch (SQLException e) {
                System.err.println("Error BD: " + e.getMessage());
            }
        }).start();
    }
}