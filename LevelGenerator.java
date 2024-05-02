public class LevelGenerator {

    // Méthode principale pour réorganiser les rangées du niveau
    public static void reorderRows(int[][] level) {
        int numRows = level.length;
        
        // Tableaux pour stocker les valeurs des trésors et des monstres pour chaque rangée
        int[] treasuresValues = new int[numRows];
        int[] monstersStrengths = new int[numRows];
        
        // Calculer les valeurs de trésors et de monstres pour chaque rangée
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < level[i].length; j++) {
                if (level[i][j] > 0) { // Trésor
                    treasuresValues[i] += level[i][j];
                } else if (level[i][j] < 0) { // Monstre
                    monstersStrengths[i] += Math.abs(level[i][j]); // Utiliser une valeur positive pour les monstres
                }
            }
        }

        // Réorganiser les rangées en fonction de la différence entre les valeurs de trésors et de monstres
        for (int i = 0; i < numRows; i++) {
            for (int j = i + 1; j < numRows; j++) {
                int diffI = treasuresValues[i] - monstersStrengths[i];
                int diffJ = treasuresValues[j] - monstersStrengths[j];
                if (diffI < diffJ) {
                    swapRows(level, i, j);
                    swapValues(treasuresValues, i, j);
                    swapValues(monstersStrengths, i, j);
                }
            }
        }
    }

    // Méthode pour échanger deux rangées dans la grille
    private static void swapRows(int[][] level, int row1, int row2) {
        int[] temp = level[row1];
        level[row1] = level[row2];
        level[row2] = temp;
    }

    // Méthode pour échanger deux valeurs dans un tableau
    private static void swapValues(int[] array, int index1, int index2) {
        int temp = array[index1];
        array[index1] = array[index2];
        array[index2] = temp;
    }
    
    // Méthode principale pour tester
    public static void main(String[] args) {
        // Exemple de génération d'un niveau
        int[][] level = {
            {5, -2, 0, 0, 8},
            {0, 0, 0, 0, 0},
            {0, -10, 0, 6, 0}
        };

        // Réorganiser les rangées
        reorderRows(level);

        // Afficher le niveau réorganisé
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[i].length; j++) {
                System.out.print(level[i][j] + " ");
            }
            System.out.println();
        }
    }
}
