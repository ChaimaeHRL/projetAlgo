import java.util.Random;

public class PlateauGenerator {
    private static final int MAX_TRESOR_VALEUR = 99;
    private static final int MAX_MONSTRE_VALEUR = 50;

    public static void genererPlateau(int[][] plateau) {
        Random random = new Random();

        // Parcourir chaque case du plateau
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                // Générer un nombre aléatoire entre 0 et 5 inclus
                int randomNumber = random.nextInt(6);
                
                // Placer un trésor si le nombre est 0
                if (randomNumber == 0) {
                    plateau[i][j] = random.nextInt(MAX_TRESOR_VALEUR) + 1;
                }
                // Placer un monstre si le nombre est 1 ou 2
                else if (randomNumber == 1 || randomNumber == 2) {
                    plateau[i][j] = -1 * (random.nextInt(MAX_MONSTRE_VALEUR) + 1);
                }
                // Laisser la case vide sinon
                else {
                    plateau[i][j] = 0;
                }
            }
        }

        // Vérifier les contraintes pour chaque rangée
        for (int i = 0; i < plateau.length; i++) {
            int tresorTotal = 0;
            int monstreTotal = 0;

            for (int j = 0; j < plateau[i].length; j++) {
                // Calculer la somme des trésors et des monstres dans la rangée
                if (plateau[i][j] > 0) {
                    tresorTotal += plateau[i][j];
                } else if (plateau[i][j] < 0) {
                    monstreTotal -= plateau[i][j];
                }
            }

            // Vérifier les contraintes
            if (monstreTotal < 2 || monstreTotal > 2 || tresorTotal > monstreTotal) {
                // Réinitialiser la rangée si les contraintes ne sont pas respectées
                for (int j = 0; j < plateau[i].length; j++) {
                    plateau[i][j] = 0;
                }
                // Regénérer la rangée
                genererPlateau(plateau);
                break;
            }
        }
    }

    public static void main(String[] args) {
        int[][] plateau = new int[7][11];
        genererPlateau(plateau);

        // Afficher le plateau généré
        for (int i = 0; i < plateau.length; i++) {
            for (int j = 0; j < plateau[i].length; j++) {
                System.out.print(plateau[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
