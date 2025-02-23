import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.Normalizer;
import java.util.*;
import java.util.List;

public class FenetreJeu extends JFrame {
    private JLabel motLabel; // Label pour afficher le mot masqué
    private JLabel essaisLabel; // Label pour afficher les essais restants
    private JLabel scoreLabel; // Label pour afficher le score
    private JLabel penduLabel; // Label pour afficher l'image du pendu
    private String motSecret; // Mot à deviner
    private char[] motAffiche; // Version du mot avec des underscores
    private int essaisRestants; // Nombre d'essais restants
    private int score; // Score du joueur
    private ImageIcon[] penduImages; // Tableau d'images pour le pendu
    private JButton[] clavierButtons; // Boutons du clavier virtuel
    private String nomJoueur; // Nom du joueur

    // Constructeur de FenetreJeu
    public FenetreJeu() {
        // Demander le nom du joueur avec un pop-up amélioré
        this.nomJoueur = demanderNomJoueur();
        if (this.nomJoueur == null || this.nomJoueur.trim().isEmpty()) {
            this.nomJoueur = "Joueur"; // Nom par défaut si aucun nom n'est entré
        }

        // Choisir un mot aléatoire
        this.motSecret = choisirMotAleatoire();
        this.motAffiche = new char[motSecret.length()]; // Initialisation du tableau
        this.essaisRestants = 6; // Nombre d'essais restants (par défaut 6)
        this.score = 100; // Score de départ

        // Charger les images du pendu
        chargerImagesPendu();

        // Remplir le motAffiche avec des underscores
        for (int i = 0; i < motSecret.length(); i++) {
            motAffiche[i] = '_';
        }

        // Configurer la fenêtre
        this.setTitle("Jeu du Pendu");
        this.setSize(600, 700); // Taille ajustée pour inclure le clavier
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // Centrer la fenêtre à l'écran
        this.setResizable(false);

        // Créer la barre de menu
        creerBarreMenu();

        // Créer l'interface graphique
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Ajouter des marges

        // Label pour afficher le mot masqué
        motLabel = new JLabel(formaterMotAffiche());
        motLabel.setFont(new Font("Arial", Font.BOLD, 30));
        motLabel.setAlignmentX(CENTER_ALIGNMENT);
        motLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10)); // Ajouter des marges

        // Label pour afficher le nombre d'essais restants
        essaisLabel = new JLabel("Essais restants : " + essaisRestants);
        essaisLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        essaisLabel.setAlignmentX(CENTER_ALIGNMENT);
        essaisLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10)); // Ajouter des marges

        // Label pour afficher le score
        scoreLabel = new JLabel("Score : " + score);
        scoreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        scoreLabel.setAlignmentX(CENTER_ALIGNMENT);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10)); // Ajouter des marges

        // Label pour afficher l'image du pendu
        penduLabel = new JLabel(penduImages[6 - essaisRestants]);
        penduLabel.setAlignmentX(CENTER_ALIGNMENT);
        penduLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10)); // Ajouter des marges

        // Créer le clavier virtuel
        JPanel clavierPanel = creerClavier();
        container.add(motLabel);
        container.add(essaisLabel);
        container.add(scoreLabel);
        container.add(penduLabel);
        container.add(clavierPanel);

        this.setContentPane(container);
        this.setVisible(true);
    }

    // Méthode pour demander le nom du joueur avec un pop-up amélioré
    private String demanderNomJoueur() {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Entrez votre nom :");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        JTextField textField = new JTextField(20);
        panel.add(label, BorderLayout.NORTH);
        panel.add(textField, BorderLayout.CENTER);

        int option = JOptionPane.showOptionDialog(
                this,
                panel,
                "Nom du joueur",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE,
                null,
                new Object[]{"OK", "Annuler"},
                "OK"
        );

        if (option == JOptionPane.OK_OPTION) {
            return textField.getText().trim();
        }
        return "Joueur"; // Retourner un nom par défaut si l'utilisateur annule
    }

    // Méthode pour créer la barre de menu
    private void creerBarreMenu() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(59, 130, 246)); // Couleur de fond du menu
        menuBar.setForeground(Color.WHITE); // Couleur du texte du menu

        // Menu Fichier
        JMenu fichierMenu = new JMenu("Fichier");
        fichierMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        fichierMenu.setForeground(Color.WHITE);

        JMenuItem nouvellePartieItem = new JMenuItem("Nouvelle partie");
        nouvellePartieItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        nouvellePartieItem.addActionListener(e -> resetJeu());

        JMenuItem afficherScoreItem = new JMenuItem("Afficher les scores");
        afficherScoreItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        afficherScoreItem.addActionListener(e -> afficherScores());

        JMenuItem retourItem = new JMenuItem("Retour");
        retourItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        retourItem.addActionListener(e -> {
            this.dispose();
            new FenetrePrincipale().setVisible(true); // Retour à la fenêtre principale
        });

        fichierMenu.add(nouvellePartieItem);
        fichierMenu.add(afficherScoreItem);
        fichierMenu.add(retourItem);

        // Menu Aide
        JMenu aideMenu = new JMenu("Aide");
        aideMenu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        aideMenu.setForeground(Color.WHITE);

        JMenuItem reglesItem = new JMenuItem("Règles du jeu");
        reglesItem.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        reglesItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Devinez le mot en proposant des lettres. Vous avez 6 essais.", "Règles", JOptionPane.INFORMATION_MESSAGE));

        aideMenu.add(reglesItem);

        menuBar.add(fichierMenu);
        menuBar.add(aideMenu);
        this.setJMenuBar(menuBar);
    }

    // Méthode pour créer le clavier virtuel
    private JPanel creerClavier() {
        JPanel clavierPanel = new JPanel();
        clavierPanel.setLayout(new GridLayout(3, 9, 5, 5)); // 3 lignes, 9 colonnes, espacement de 5px
        clavierButtons = new JButton[26]; // 26 boutons pour chaque lettre de l'alphabet

        for (char c = 'A'; c <= 'Z'; c++) {
            JButton button = new JButton(String.valueOf(c));
            button.setFont(new Font("Arial", Font.BOLD, 18));
            button.setFocusPainted(false);
            button.addActionListener(e -> {
                button.setEnabled(false); // Désactiver le bouton après clic
                devinerLettre(button.getText());
            });
            clavierButtons[c - 'A'] = button;
            clavierPanel.add(button);
        }

        return clavierPanel;
    }

    // Fonction pour formater le mot à afficher (avec des underscores)
    private String formaterMotAffiche() {
        StringBuilder sb = new StringBuilder();
        for (char c : motAffiche) {
            sb.append(c).append(" ");
        }
        return sb.toString().trim();
    }

    // Fonction pour normaliser les caractères (gestion des accents)
    private String normaliser(String texte) {
        return Normalizer.normalize(texte, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }

    // Fonction pour deviner une lettre
    private void devinerLettre(String lettre) {
        boolean lettreTrouvee = false;
        String lettreNormalisee = normaliser(lettre); // Normaliser la lettre

        for (int i = 0; i < motSecret.length(); i++) {
            String caractereMot = normaliser(String.valueOf(motSecret.charAt(i))); // Normaliser le caractère du mot
            if (caractereMot.equals(lettreNormalisee)) {
                motAffiche[i] = motSecret.charAt(i); // Afficher la lettre trouvée
                lettreTrouvee = true;
            }
        }

        if (!lettreTrouvee) {
            essaisRestants--;
            score -= 100 / motSecret.length(); // Diminuer le score proportionnellement
        }

        // Mettre à jour les labels
        motLabel.setText(formaterMotAffiche());
        essaisLabel.setText("Essais restants : " + essaisRestants);
        scoreLabel.setText("Score : " + score);
        penduLabel.setIcon(penduImages[6 - essaisRestants]);

        // Vérifier si le joueur a gagné ou perdu
        if (String.valueOf(motAffiche).equals(motSecret)) {
            JOptionPane.showMessageDialog(this, "Félicitations ! Vous avez gagné !", "Victoire", JOptionPane.INFORMATION_MESSAGE);
            enregistrerScore();
            resetJeu();
        } else if (essaisRestants == 0) {
            score = Math.max(0, score); // Assurer que le score ne soit pas négatif
            JOptionPane.showMessageDialog(this, "Dommage, vous avez perdu. Le mot était : " + motSecret, "Perte", JOptionPane.INFORMATION_MESSAGE);
            enregistrerScore();
            resetJeu();
        }
    }

    // Méthode pour réinitialiser le jeu
    private void resetJeu() {
        this.motSecret = choisirMotAleatoire(); // Choisir un nouveau mot aléatoire
        this.motAffiche = new char[motSecret.length()];
        for (int i = 0; i < motSecret.length(); i++) {
            motAffiche[i] = '_';
        }
        this.essaisRestants = 6;
        this.score = 100; // Réinitialiser le score à 100
        motLabel.setText(formaterMotAffiche());
        essaisLabel.setText("Essais restants : " + essaisRestants);
        scoreLabel.setText("Score : " + score);
        penduLabel.setIcon(penduImages[0]);

        // Réactiver tous les boutons du clavier
        for (JButton button : clavierButtons) {
            button.setEnabled(true);
        }
    }

    // Méthode pour charger les images du pendu
    private void chargerImagesPendu() {
        penduImages = new ImageIcon[7];
        for (int i = 0; i < 7; i++) {
            penduImages[i] = new ImageIcon("images/Pendu-" + i + ".png"); // Assurez-vous d'avoir les images pendu0.png à pendu6.png
        }
    }

    // Méthode pour lire les mots depuis un fichier et en choisir un aléatoirement
    private String choisirMotAleatoire() {
        List<String> mots = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("dictionnaire.txt"))) {
            String ligne;
            while ((ligne = br.readLine()) != null) {
                mots.add(normaliser(ligne.trim().toUpperCase())); // Normaliser les mots
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la lecture du fichier dictionnaire.txt.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return "EXEMPLE"; // Retourner un mot par défaut en cas d'erreur
        }

        if (mots.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Le fichier dictionnaire.txt est vide.", "Erreur", JOptionPane.ERROR_MESSAGE);
            return "EXEMPLE"; // Retourner un mot par défaut si le fichier est vide
        }

        Random random = new Random();
        return mots.get(random.nextInt(mots.size()));
    }

    // Méthode pour enregistrer le score dans un fichier
    private void enregistrerScore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt", true))) {
            writer.write(nomJoueur + " : " + score + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement du score.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Supprimer les trois scores les plus petits
        supprimerScoresLesPlusPetits();
    }

    // Méthode pour supprimer les trois scores les plus petits
    private void supprimerScoresLesPlusPetits() {
        try {
            // Lire tous les scores
            List<String> scores = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("scores.txt"))) {
                String ligne;
                while ((ligne = reader.readLine()) != null) {
                    scores.add(ligne);
                }
            }

            // Trier les scores par ordre croissant
            scores.sort(Comparator.comparingInt(s -> Integer.parseInt(s.split(" : ")[1])));

            // Supprimer les trois scores les plus petits
            if (scores.size() > 3) {
                scores = scores.subList(3, scores.size());
            }

            // Réécrire les scores dans le fichier
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("scores.txt"))) {
                for (String score : scores) {
                    writer.write(score + "\n");
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la gestion des scores.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Méthode pour afficher les scores enregistrés
    private void afficherScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader("scores.txt"))) {
            StringBuilder scores = new StringBuilder();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                scores.append(ligne).append("\n");
            }
            JOptionPane.showMessageDialog(this, scores.toString(), "Scores", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Aucun score enregistré.", "Scores", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Méthode principale pour démarrer le jeu
    public static void main(String[] args) {
        new FenetreJeu(); // Démarrer le jeu
    }
}