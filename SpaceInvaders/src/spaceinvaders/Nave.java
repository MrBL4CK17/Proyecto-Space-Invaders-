package spaceinvaders;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Nave {
    private int x, y, velocidad;
    private int ancho = 40, alto = 20;
    private int dx; // Desplazamiento en X

    public Nave(int x, int y, int velocidad) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
    }

    public void mover(int limiteAncho) {
        x += dx;

        // Limites de la pantalla para la nave
        if (x < 10) x = 10;
        if (x > limiteAncho - ancho - 10) x = limiteAncho - ancho - 10;
    }

    public void dibujar(Graphics g) {
        g.setColor(Color.GREEN);
        // Dibujamos una nave simple tipo tanque
        int[] px = {x, x + ancho, x + ancho, x + (ancho/2) + 5, x + (ancho/2) - 5, x, x};
        int[] py = {y + alto, y + alto, y + (alto/2), y + (alto/2) - 5, y + (alto/2) - 5, y + (alto/2), y + alto};
        g.fillPolygon(px, py, 7);
    }

    // Gestion de teclas presionadas
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) dx = -velocidad;
        if (key == KeyEvent.VK_RIGHT) dx = velocidad;
    }

    // Gestion de teclas soltadas
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) dx = 0;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }
}