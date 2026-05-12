package spaceinvaders;

import javax.swing.JFrame;
import java.awt.EventQueue;

public class SpaceInvaders extends JFrame {
    private Font pixelFont;//guardar la fuente

    public SpaceInvaders() {
        add(new TableroJuego(pixelFont));
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //titulo
         JLabel titulo = new JLabel(" Space  Invaders  Sistemas TecNM  ");
        titulo.setForeground(new Color(255,0, 255));
        titulo.setFont(pixelFont);
        add(titulo,BorderLayout.NORTH);
    }

 public void cargarFuenteExterna(){
         try{
             File fontFile = new File("src/spaceinvaders/arcadeclassic/ARCADECLASSIC.ttf");
             pixelFont = Font.createFont(Font.TRUETYPE_FONT,fontFile).deriveFont(30f);
             
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(pixelFont);
            
         }catch(IOException | FontFormatException e){
             e.printStackTrace();
            // Fuente de respaldo por si el archivo no existe o
            pixelFont = new Font("Monospaced", Font.BOLD, 24);
         }
     }
    
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            new SpaceInvaders().setVisible(true);
        });
    }
}
