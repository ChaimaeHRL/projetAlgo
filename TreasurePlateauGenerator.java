import java.util.*;

public class TreasurePlateauGenerator {
    public static void main(String[] args) {
        // Définir la taille du plateau
        int lignes = 11;
        int colonnes = 7;

        // Créer un tableau pour représenter le plateau de jeu
        String[][] plateau = new String[lignes][colonnes];

        // Remplir le plateau avec les trésors, les princes et les cases vides
        remplirPlateau(plateau);

        // Afficher le plateau
        afficherPlateau(plateau);
    }

    // Méthode pour remplir le plateau avec des trésors, des princes et des cases vides
    private static void remplirPlateau(String[][] plateau) {
        Random random = new Random();

        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                int randomNumber = random.nextInt(Integer.MAX_VALUE);
                int hash = randomNumber % 6;

                if (hash == 0) {
                    plateau[i][j] = "trésor";
                } else if (hash == 1 || hash == 2) {
                    plateau[i][j] = "prince";
                } else {
                    plateau[i][j] = "vide";
                }
            }
        }
    }

    // Méthode pour afficher le plateau
    private static void afficherPlateau(String[][] plateau) {
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[0].length; j++) {
                System.out.print(plateau[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
