package spaceinvaders;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TableroJuego extends JPanel implements Runnable {
    private Thread hiloPrincipal;
    private boolean enJuego = true;
    private int vidasJugador = 3;
    private int nivel = 1, puntos = 0, velocidadEnemigos = 2;
    private List<Enemigo> enemigos;
    private Random rand = new Random();
    // Nueva variable para la Nave
    private Nave nave;

     private Image imagenFondo;
    //NUEVA VARIABLE PARA LA FUENTE QUE VIENE EN LA CLASE PRINCIPAL
    private Font fuentePixel;
    
    private List<Proyectil> proyectiles = new ArrayList<>();
    public TableroJuego() {
        setBackground(Color.BLACK);
        setFocusable(true);

        //CARGAR EL FONDO
        try{
            // Asegúrate de que el nombre del archivo coincida (ejemplo: fondo.png)
        ImageIcon iconoFondo = new ImageIcon(getClass().getResource("fondo.jpg"));
        imagenFondo = iconoFondo.getImage();
    } catch (Exception e) {
        System.out.println("No se pudo cargar el fondo, se usará color negro.");
    }
        
        // CORRECCIÓN DEFINITIVA GAME OVER: Inicializamos la nave AHORA, 
        // pero esperaremos a que el panel sea visible para inicializar enemigos.
        // Esto evita que getHeight() devuelva 0.
        nave = new Nave(380, 520, 5); // Posicion inicial de la nave
        
        // Escuchador de teclado
        addKeyListener(new TAdapter());
        
        // Iniciamos el hilo, pero la lógica de enemigos esperará.
        hiloPrincipal = new Thread(this);
        hiloPrincipal.start();
    }

    private void inicializarEnemigos() {
        if (enemigos != null) {
            enemigos.clear();
        } else {
            enemigos = new ArrayList<>();
        }
        
        int cantidad = 3 + nivel; 
        for (int i = 0; i < cantidad; i++) {
            // Organización aleatoria en el tercio superior del tablero
            int xAleatoria = rand.nextInt(Math.max(1, getWidth() - 60));
            int yAleatoria = rand.nextInt(150) + 50; 
            enemigos.add(new Enemigo(xAleatoria, yAleatoria, velocidadEnemigos));
        }
    }

    @Override
    public void run() {
        // ESPERA DE SEGURIDAD: Esperamos a que el panel se dibuje una vez 
        // para tener dimensiones reales (getWidth/getHeight > 0).
        while(getWidth() == 0 || getHeight() == 0) {
            try { Thread.sleep(50); } catch (InterruptedException e) {}
        }
        
        //  inicializamos enemigos con seguridad
        inicializarEnemigos();

        while (enJuego) {
            actualizar();
            repaint();
            try { Thread.sleep(16); } catch (InterruptedException e) { break; }
        }
    }

private void actualizar() {
    if (!enJuego || enemigos == null) return;

    nave.mover(getWidth());

    // 1. Coordinar Proyectiles (Moverlos y limpiar los que se salen)
    for (int i = 0; i < proyectiles.size(); i++) {
        Proyectil p = proyectiles.get(i);
        p.mover();
        if (!p.estaVivo()) {
            proyectiles.remove(i);
            i--;
        }
    }

   // --- COORDINAR ENEMIGOS Y VIDAS ---
        for (int i = 0; i < enemigos.size(); i++) {
            Enemigo e = enemigos.get(i);
            e.mover(getWidth());

            // 1. Choque Directo o Línea de Peligro
            if (e.getBounds().intersects(nave.getBounds()) || e.getBounds().y > getHeight() - 100) {
                vidasJugador--; // Restamos una vida
                
                if (vidasJugador <= 0) {
                    finalizarJuego();
                    return;
                } else {
                    // Si quedan vidas, reiniciamos la posición de los aliens
                    inicializarEnemigos(); 
                    return; 
                }
            }

        // Choque: Proyectil vs Enemigo
        for (int j = 0; j < proyectiles.size(); j++) {
            Proyectil p = proyectiles.get(j);
            if (p.getBounds().intersects(e.getBounds())) {
                enemigos.remove(i); // El alien muere
                proyectiles.remove(j); // La bala desaparece
                puntos += 100;
                i--; // Reajustamos el índice de enemigos
                break; 
            }
        }
    }
    

    // Si no quedan enemigos, subes de nivel (esto ya lo tienes)
    if (enemigos.isEmpty()) {
        nivel++;
        velocidadEnemigos++;
        inicializarEnemigos();
    }
}

    private void finalizarJuego() {
        enJuego = false;
        SwingUtilities.invokeLater(() -> {
            String nombre = JOptionPane.showInputDialog(this, 
                "¡GAME OVER!\nNivel alcanzado: " + nivel + "\nPuntos: " + puntos + "\nTu nombre:");
            
            if (nombre == null || nombre.isEmpty()) nombre = "Jugador";
            
            DBHandler.guardarPuntaje(nombre, puntos, nivel);
            System.exit(0);
        });
    }

@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 1. Configuración de gráficos 2D
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

// --- DIBUJAR UI CON VIDAS ---
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        
        g.setColor(Color.WHITE);
        g.drawString("VIDAS: " + vidasJugador, 10, 40); // Texto de vidas

         // 2. DIBUJAR EL FONDO 
    if (imagenFondo != null) {
        // Esto estira la imagen para que ocupe toda la ventana
        g2d.drawImage(imagenFondo, 0, 0, getWidth(), getHeight(), null);
    }

        // 3.INICIA A  USAR LA FUENTE PIXEL EN EL JUEGO
        if (fuentePixel != null) {
            g2d.setFont(fuentePixel.deriveFont(25f)); // Tamaño 18 para el marcador
        } else {
            g2d.setFont(new Font("Monospaced", Font.BOLD, 18)); // Respaldo
        }
        
        // 4. Dibujar Puntuación y Nivel
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("NIVEL: " + nivel + " | PUNTOS: " + puntos, 10, 20);
        
        // 5. Línea de peligro
        g.setColor(new Color(255, 0, 0, 80));
        g.drawLine(0, getHeight() - 100, getWidth(), getHeight() - 100);

        // 6. Dibujar la Nave
        if (nave != null) {
            nave.dibujar(g);

     // 7. Dibujar Enemigos
        if (enemigos != null) {
            for (Enemigo e : enemigos) e.dibujar(g);
        }
            // --- CÓDIGO DE LA MIRA (DENTRO DEL BLOQUE DE LA NAVE) ---
            Rectangle naveBounds = nave.getBounds();
            int xCentroNave = naveBounds.x + (naveBounds.width / 2);
            int yMira = naveBounds.y - 100; 

            g2d.setColor(Color.GREEN); 
            g2d.setStroke(new BasicStroke(2)); 
            g2d.drawOval(xCentroNave - 20, yMira - 20, 40, 40); // Círculo
            g2d.drawLine(xCentroNave, yMira - 15, xCentroNave, yMira + 15); // Cruz vertical
            g2d.drawLine(xCentroNave - 15, yMira, xCentroNave + 15, yMira); // Cruz horizontal
        }

        // 8. Dibujar los Proyectiles (Balas)
        if (proyectiles != null) {
            for (Proyectil p : proyectiles) {
                p.dibujar(g);
            }
        }

        // 9. Dibujar los Enemigos (Aliens)
        if (enemigos != null) {
            for (Enemigo e : enemigos) {
                e.dibujar(g);
            }
        }
        
        // Sincronización para evitar lag visual
        Toolkit.getDefaultToolkit().sync();
    }


    // Clase interna para gestionar el teclado
private class TAdapter extends KeyAdapter {
    @Override
    public void keyPressed(KeyEvent e) {
        nave.keyPressed(e);
        // Si presiona espacio, disparamos
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // El disparo sale de la mitad de la nave
            int posX = nave.getBounds().x + (nave.getBounds().width / 2) - 3;
            int posY = nave.getBounds().y;
            proyectiles.add(new Proyectil(posX, posY));
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        nave.keyReleased(e);
    }
}
}
