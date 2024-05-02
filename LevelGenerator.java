public class LevelGenerator {

    // Méthode principale pour générer un niveau aléatoire
    public static int[][] generateRandomLevel(int rows, int cols) {
        int[][] level = new int[rows][cols];
        generateRandomLevelRecursive(level, 0, rows - 1, cols);
        return level;
    }

    // Méthode récursive pour générer aléatoirement chaque rangée de la grille
    private static void generateRandomLevelRecursive(int[][] level, int startRow, int endRow, int cols) {
        if (startRow > endRow) {
            return; // Cas de base : toutes les rangées ont été générées
        }

        int midRow = startRow + (endRow - startRow) / 2; // Calcul du milieu des rangées

        // Générer aléatoirement la rangée médiane
        generateRandomRow(level[midRow], cols);

        // Appeler récursivement pour les sous-problèmes des rangées supérieures et inférieures
        generateRandomLevelRecursive(level, startRow, midRow - 1, cols);
        generateRandomLevelRecursive(level, midRow + 1, endRow, cols);
    }

    // Méthode pour générer aléatoirement une rangée de la grille
    private static void generateRandomRow(int[] row, int cols) {
        for (int i = 0; i < cols; i++) {
            // Générer aléatoirement soit un trésor, soit un monstre, soit une case vide
            int randomValue = (int) (Math.random() * 6); // Valeur entre 0 et 5
            if (randomValue == 0) {
                // Case vide
                row[i] = 0;
            } else if (randomValue == 1 || randomValue == 2) {
                // Trésor (valeurs entre 1 et 99)
                row[i] = (int) (Math.random() * 99) + 1;
            } else {
                // Monstre (valeurs entre 1 et 50)
                row[i] = (int) (Math.random() * 50) + 1;
            }
        }
    }
}
