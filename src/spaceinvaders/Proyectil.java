
package spaceinvaders;

import java.awt.*;

public class Proyectil {
    // Datos de la bala
    private int x, y;
    private final int ANCHO = 5, ALTO = 12;
    private final int VELOCIDAD = 7;
    private boolean activo = true; // Para saber si sigue en pantalla

    // Constructor: posición donde sale
    public Proyectil(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Se mueve hacia ARRIBA
    public void mover() {
        y -= VELOCIDAD;
        if (y < 0) activo = false; // Si se sale por arriba, muere
    }

    // Dibujar la bala
    public void dibujar(Graphics g) {
        if (activo) {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, ANCHO, ALTO);
        }
    }

    // Área para detectar choques
    public Rectangle getBounds() {
        return new Rectangle(x, y, ANCHO, ALTO);
    }

    // ¿Sigue viva?
    public boolean estaActivo() {
        return activo;
    }

    // Al chocar, destruir
    public void destruir() {
        activo = false;
    }
}