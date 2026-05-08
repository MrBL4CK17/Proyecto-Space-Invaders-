package spaceinvaders;

import javax.swing.JFrame;
import java.awt.EventQueue;

public class SpaceInvaders extends JFrame {
    public SpaceInvaders() {
        add(new TableroJuego());
        setTitle("Space Invaders - Sistemas TecNM");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new SpaceInvaders().setVisible(true);
        });
    }
}