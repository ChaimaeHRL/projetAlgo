import java.util.Arrays;

public class LevelGenerator {
    
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
    
    public static void reorderRows(int[][] level) {
        // Calculer les valeurs de trésors et de monstres pour chaque rangée
        int numRows = level.length;
        int[] rowValues = new int[numRows];
        for (int i = 0; i < numRows; i++) {
            int treasureSum = Arrays.stream(level[i]).filter(value -> value > 0).sum();
            int monsterSum = Arrays.stream(level[i]).filter(value -> value < 0).map(Math::abs).sum();
            rowValues[i] = treasureSum - monsterSum;
        }

        // Appliquer le tri fusion en utilisant les valeurs calculées pour chaque rangée
        triFusion(level, rowValues, 0, numRows - 1);
    }

    public static void triFusion(int[][] level, int[] rowValues, int debut, int fin) {
        if (debut < fin) {
            int milieu = (debut + fin) / 2;
            triFusion(level, rowValues, debut, milieu);
            triFusion(level, rowValues, milieu + 1, fin);
            fusionner(level, rowValues, debut, milieu, fin);
        }
    }

    public static void fusionner(int[][] level, int[] rowValues, int debut, int milieu, int fin) {
        int n1 = milieu - debut + 1;
        int n2 = fin - milieu;

        int[][] gauche = new int[n1][];
        int[] gaucheValues = new int[n1];
        int[][] droite = new int[n2][];
        int[] droiteValues = new int[n2];

        for (int i = 0; i < n1; ++i) {
            gauche[i] = level[debut + i];
            gaucheValues[i] = rowValues[debut + i];
        }
        for (int j = 0; j < n2; ++j) {
            droite[j] = level[milieu + 1 + j];
            droiteValues[j] = rowValues[milieu + 1 + j];
        }

        int i = 0, j = 0;
        int k = debut;
        while (i < n1 && j < n2) {
            if (gaucheValues[i] >= droiteValues[j]) {
                level[k] = gauche[i];
                rowValues[k] = gaucheValues[i];
                i++;
            } else {
                level[k] = droite[j];
                rowValues[k] = droiteValues[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            level[k] = gauche[i];
            rowValues[k] = gaucheValues[i];
            i++;
            k++;
        }

        while (j < n2) {
            level[k] = droite[j];
            rowValues[k] = droiteValues[j];
            j++;
            k++;
        }
    }
}
