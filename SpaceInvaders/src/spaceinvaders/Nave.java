package spaceinvaders;

import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Nave {
    private int x, y, velocidad;
    private int ancho = 40, alto = 20;
    private int dx; // Desplazamiento en X
    private Image imagenNave;
    
    public Nave(int x, int y, int velocidad) {
        this.x = x;
        this.velocidad = velocidad;
        cargarImagen();
        this.y = y - this.alto;
        
    }
    
    private void cargarImagen() {
        // Carga la imagen desde la ruta de archivos del proyecto
        ImageIcon ii = new ImageIcon(getClass().getResource("snoopy.png"));
        this.imagenNave = ii.getImage();
        
        // Opcional: Si quieres que el ancho y alto se ajusten automáticamente al PNG:
        this.ancho = imagenNave.getWidth(null);
        this.alto = imagenNave.getHeight(null);
    }

    public void mover(int limiteAncho) {
        x += dx;

        // Limites de la pantalla para la nave
        if (x < 10) x = 10;
        if (x > limiteAncho - ancho - 10) x = limiteAncho - ancho - 10;
    }

    public void dibujar(Graphics g) {
        /*g.setColor(Color.GREEN);
        // Dibujamos una nave simple tipo tanque
        int[] px = {x, x + ancho, x + ancho, x + (ancho/2) + 5, x + (ancho/2) - 5, x, x};
        int[] py = {y + alto, y + alto, y + (alto/2), y + (alto/2) - 5, y + (alto/2) - 5, y + (alto/2), y + alto};
        g.fillPolygon(px, py, 7);*/
        g.drawImage(imagenNave, x, y, ancho, alto, null);
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