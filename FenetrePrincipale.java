import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class FenetrePrincipale extends JFrame {

    // Palette de couleurs professionnelle
    private static final Color PRIMARY_COLOR = new Color(59, 130, 246); // Bleu vif
    private static final Color SECONDARY_COLOR = new Color(255, 255, 255); // Blanc
    private static final Color BACKGROUND_COLOR = new Color(249, 250, 251); // Gris clair
    private static final Color TEXT_COLOR = new Color(31, 41, 55); // Gris foncé
    private static final Color ACCENT_COLOR = new Color(252, 211, 77); // Jaune doré

    public FenetrePrincipale() {
        // Configuration de la fenêtre
        configureFenetre();

        // Création du conteneur principal
        JPanel container = createMainContainer();

        // Ajout des composants
        container.add(Box.createVerticalGlue()); // Espacement en haut
        container.add(createTitreLabel());
        container.add(Box.createRigidArea(new Dimension(0, 30))); // Espacement
        container.add(createImagePanel());
        container.add(Box.createRigidArea(new Dimension(0, 40))); // Espacement
        container.add(createStartButton());
        container.add(Box.createVerticalGlue()); // Espacement en bas

        this.setContentPane(container);
        this.setVisible(true);
    }

    private void configureFenetre() {
        this.setTitle("Le Pendu - Jeu de devinette");
        this.setSize(900, 1000); // Taille augmentée pour plus d'espace
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null); // Centrer la fenêtre
        this.setResizable(false);
        this.getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private JPanel createMainContainer() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(BACKGROUND_COLOR);
        container.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Espacement généreux
        return container;
    }

    private JLabel createTitreLabel() {
        JLabel label = new JLabel("Bienvenue dans le jeu du PENDU");
        label.setFont(new Font("Segoe UI", Font.BOLD, 38)); // Police moderne
        label.setForeground(TEXT_COLOR);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Ombre légère pour le texte
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return label;
    }

    private JPanel createImagePanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        try {
            Image originalImage = ImageIO.read(new File("images/Pendu-6.png"));
            Image scaledImage = originalImage.getScaledInstance(300, 300, Image.SCALE_SMOOTH); // Image agrandie
            ImageIcon icon = new ImageIcon(scaledImage);

            JLabel imageLabel = new JLabel(icon) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    // Rendering hints pour une meilleure qualité
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

                    // Ombre portée plus prononcée
                    int shadowSize = 15;
                    g2.setColor(new Color(0, 0, 0, 50));
                    g2.fillRoundRect(shadowSize, shadowSize,
                            getWidth() - shadowSize * 2,
                            getHeight() - shadowSize * 2, 30, 30);

                    // Image avec bordure arrondie
                    g2.clipRect(0, 0, getWidth() - shadowSize, getHeight() - shadowSize);
                    super.paintComponent(g2);
                    g2.dispose();
                }
            };

            imageLabel.setOpaque(false);
            imageLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
            panel.add(imageLabel);

        } catch (IOException e) {
            JLabel errorLabel = new JLabel("Image non chargée");
            errorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            errorLabel.setForeground(new Color(200, 200, 200));
            panel.add(errorLabel);
        }
        return panel;
    }

    private JButton createStartButton() {
        JButton button = new JButton("COMMENCER") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Dégradé de fond
                GradientPaint gradient = new GradientPaint(
                        0, 0, PRIMARY_COLOR,
                        getWidth(), getHeight(), PRIMARY_COLOR.darker()
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Bordure arrondie

                // Ombre portée
                g2.setColor(new Color(0, 0, 0, 50));
                g2.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 30, 30);

                // Texte centré
                g2.setColor(SECONDARY_COLOR);
                FontMetrics metrics = g2.getFontMetrics();
                int x = (getWidth() - metrics.stringWidth(getText())) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2.drawString(getText(), x, y);

                g2.dispose();
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 28)); // Police plus grande
        button.setForeground(SECONDARY_COLOR);
        button.setContentAreaFilled(false); // Désactiver le remplissage par défaut
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(25, 80, 25, 80)); // Taille généreuse

        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR.brighter());
                button.repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
                button.repaint();
            }
        });

        // Animation au clic
        button.addActionListener(e -> {
            button.setBackground(ACCENT_COLOR);
            new FenetreJeu(); // Lancer la fenêtre de jeu
            this.dispose(); // Fermer la fenêtre principale
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new FenetrePrincipale();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}