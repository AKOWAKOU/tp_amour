import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class LireFichier {

    public static void main(String[] args) {
        
        File file = new File("dictionnaire.txt");
        Random random = new Random();
        ArrayList<String> mots = new ArrayList<>();

        // Lecture du fichier et ajout des mots dans une liste
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                mots.add(line);
            }

            // Sélection d'un mot aléatoire
            if (!mots.isEmpty()) {
                String motADeviner = mots.get(random.nextInt(mots.size()));
                System.out.println("Mot aléatoire : " + motADeviner);
            } else {
                System.out.println("Le fichier est vide.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Le fichier n'a pas été trouvé.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier.");
            e.printStackTrace();
        }
    }
}
