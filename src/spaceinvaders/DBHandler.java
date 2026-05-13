package spaceinvaders;

import java.sql.*;

public class DBHandler {
    private static final String URL = "jdbc:mysql://localhost:3306/space_invaders?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASS = "sistemas"; 

    public static void guardarPuntaje(String nombre, int puntos, int nivel) {
        new Thread(() -> {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
                 PreparedStatement stmt = conn.prepareStatement(
                     "INSERT INTO puntajes (nombre, puntos, nivel_alcanzado) VALUES (?, ?, ?)")) {

                stmt.setString(1, nombre);
                stmt.setInt(2, puntos);
                stmt.setInt(3, nivel);
                stmt.executeUpdate();
                System.out.println("¡GUARDADO CORRECTO! CONEXIÓN FUNCIONA AL 100%");

            } catch (SQLException e) {
                System.err.println("❌ ERROR: " + e.getMessage());
            }
        }).start();
    }

    // ✅ Método para probar
    public static void main(String[] args) {
        System.out.println("🔄 Probando conexión...");
        guardarPuntaje("JugadorPrueba", 3000, 7);
    }
}