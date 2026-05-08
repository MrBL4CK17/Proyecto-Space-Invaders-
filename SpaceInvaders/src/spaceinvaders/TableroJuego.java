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
    private int nivel = 1, puntos = 0, velocidadEnemigos = 2;
    private List<Enemigo> enemigos;
    private Random rand = new Random();
    
    // Nueva variable para la Nave
    private Nave nave;

    public TableroJuego() {
        setBackground(Color.BLACK);
        setFocusable(true);
        
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
        enemigos = new ArrayList<>();
        int cantidad = 3 + nivel; 
        for (int i = 0; i < cantidad; i++) {
            // Spawn seguro en la parte superior
            int xAleatoria = rand.nextInt(700);
            int yAleatoria = rand.nextInt(100) + 50; 
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
        
        // Ahora sí, inicializamos enemigos con seguridad
        inicializarEnemigos();

        while (enJuego) {
            actualizar();
            repaint();
            try { Thread.sleep(16); } catch (InterruptedException e) { break; }
        }
    }

    private void actualizar() {
        if (!enJuego || enemigos == null) return;

        // Mover Nave
        nave.mover(getWidth());

        // Mover Enemigos y check Game Over
        for (int i = 0; i < enemigos.size(); i++) {
            Enemigo e = enemigos.get(i);
            e.mover(getWidth());

            // Límite de Game Over corregido (100px desde el fondo real)
            if (e.getBounds().y > getHeight() - 100) {
                finalizarJuego();
                return;
            }
        }

        // Simulación de puntos (eliminar cuando haya disparos)
        if (!enemigos.isEmpty() && rand.nextInt(100) > 98) {
            enemigos.remove(0);
            puntos += 100 * nivel;
        }

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
        
        // Suavizado de bordes
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("NIVEL: " + nivel + " | PUNTOS: " + puntos, 10, 20);
        
        // Línea de peligro
        g.setColor(new Color(255, 0, 0, 80));
        g.drawLine(0, getHeight() - 100, getWidth(), getHeight() - 100);

        // Dibujar Nave
        if (nave != null) nave.dibujar(g);

        // Dibujar Enemigos
        if (enemigos != null) {
            for (Enemigo e : enemigos) e.dibujar(g);
        }
        
        Toolkit.getDefaultToolkit().sync();
    }

    // Clase interna para gestionar el teclado
    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            nave.keyReleased(e);
        }
        @Override
        public void keyPressed(KeyEvent e) {
            nave.keyPressed(e);
        }
    }
}