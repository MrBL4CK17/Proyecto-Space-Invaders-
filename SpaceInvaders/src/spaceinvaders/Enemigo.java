package spaceinvaders;

import java.awt.*;
import java.util.Random;
import javax.swing.ImageIcon;

public class Enemigo {
    private int x, y, velocidad, direccionX;
    private int ancho = 50, alto = 50;
    private Random random = new Random();
    private int contadorPasos = 0;
    private Image imagenEnemigo;
     private int vida;
   private int vidaEnemigo;
    
   public Enemigo(int x, int y, int velocidad) {
        this.x = x;
        this.y = y;
        
        // PASO A: Creatividad - Velocidades y Vida aleatoria
        this.velocidad = velocidad + random.nextInt(3); 
        this.vidaEnemigo = (random.nextInt(10) > 8) ? 2 : 1; // Unos aguantan más
        
        this.direccionX = random.nextBoolean() ? 1 : -1;
        cargarImagen();
    }

public void recibirDanio() {
    this.vida--;
}

public int getVida() {
    return vida;
}
    
    private void cargarImagen() {
        // Carga la imagen desde la ruta de archivos del proyecto
        ImageIcon ii = new ImageIcon(getClass().getResource("alien-magenta.png"));
        this.imagenEnemigo = ii.getImage();
    }
    

   public void mover(int limiteAncho) {
        //  PASO CREATIVO (Integrado de imagen): El Teletransporte
        // El 2% de probabilidad de que el enemigo se mueva rápido a un lado
        if (random.nextInt(100) < 4) {
            x += (random.nextBoolean() ? 30 : -20); // El pequeño salto lateral
        }
        

        // PASO B: Cambio de dirección errático
        if (contadorPasos % 60 == 0 && random.nextInt(10) > 7) {
            direccionX *= -1;
        }
        
        x += direccionX * velocidad;

        // Rebote en los bordes
        if (x < 0) {
            x = 0;
            direccionX = 1;
        } else if (x > limiteAncho - ancho) {
            x = limiteAncho - ancho;
            direccionX = -1;
        }
        
        // PASO C: Descenso aleatorio
        if (random.nextInt(1000) > 996) {
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
        // Métodos para cuando agreguen los disparos
    public int getVidaEnemigo() { return vidaEnemigo; }
    public void restarVidaEnemigo() { vidaEnemigo--; }
}
    

