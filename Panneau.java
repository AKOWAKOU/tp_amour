import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
public class Panneau extends JPanel {
    private int posX = -50;
    private int posY = -50;

    /* Cette fonction s'execute automatique après le lancement du script. 
     * Elle fait une sorte d'initialisation en dessinant la forme de notre objet.
     */
    public void paintComponent(Graphics g)
    {
        //On choisit une couleur de fond pour le rectangle
        g.setColor(Color.white);
        //On le dessine de sorte qu'il occupe toute la surface
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        //On redéfinit une couleur pour le rond
        g.setColor(Color.BLUE);
        //On le dessine aux coordonnées souhaitées
        g.fillOval(posX, posY, 50, 50);
    }
    
    /* Ici, ceux sont les definitions des accesseurs et des mutateurs qui vont chercher et modifier
     les valeurs de nos variables déclarées private  */

    public int getPosX() 
    {
        return posX;
    }

    public void setPosX(int posX) 
    {
        this.posX = posX;
    }

    public int getPosY() 
    {
        return posY;
    }

    public void setPosY(int posY) 
    {
        this.posY = posY;
    }        

}