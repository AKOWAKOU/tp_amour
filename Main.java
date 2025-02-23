import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // Assurez-vous de l'initialisation de l'interface dans le thread d'événements Swing
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Créer et afficher la fenêtre principale
                FenetrePrincipale app = new FenetrePrincipale();
                app.setVisible(true);
            }
        });
    }
}
