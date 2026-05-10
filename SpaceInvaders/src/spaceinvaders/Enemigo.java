package spaceinvaders;

import java.awt.*;
import java.util.Random;
import javax.swing.ImageIcon;

public class Enemigo {
    private int x, y, velocidad, direccionX;
    private int ancho = 30, alto = 30;
    private Random random = new Random();
    private int contadorPasos = 0;
    private Image imagenEnemigo;

    public Enemigo(int x, int y, int velocidad) {
        this.x = x;
        this.y = y;
        this.velocidad = velocidad;
        // Dirección inicial aleatoria
        this.direccionX = random.nextBoolean() ? 1 : -1;
        cargarImagen();
    }
    
    private void cargarImagen() {
        // Carga la imagen desde la ruta de archivos del proyecto
        ImageIcon ii = new ImageIcon(getClass().getResource("alien-magenta.png"));
        this.imagenEnemigo = ii.getImage();
    }
    

    public void mover(int limiteAncho) {
        // Cambia de dirección horizontal ocasionalmente
        if (contadorPasos % 50 == 0 && random.nextInt(10) > 7) {
            direccionX *= -1;
        }
        
        x += direccionX * velocidad;

        // Rebote en los bordes laterales
        if (x < 0) {
            x = 0;
            direccionX = 1;
        } else if (x > limiteAncho - ancho) {
            x = limiteAncho - ancho;
            direccionX = -1;
        }
        
        // DESCENSO CORREGIDO: Solo baja muy de vez en cuando (probabilidad baja)
        if (random.nextInt(1000) > 990) {
            y += 15;
        }
        
        contadorPasos++;
    }

    public void dibujar(Graphics g) {
        /*g.setColor(Color.RED);
        g.fillRect(x, y, ancho, alto);
        g.setColor(Color.WHITE);
        g.fillRect(x + 5, y + 5, 5, 5);
        g.fillRect(x + 20, y + 5, 5, 5);
*/
        if (imagenEnemigo != null) {
            // Dibuja el PNG en las coordenadas x, y con el tamaño definido
            g.drawImage(imagenEnemigo, x, y, ancho, alto, null);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, ancho, alto);
    }
}