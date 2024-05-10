import java.util.Arrays;

/* --- Divide & Conquer --- */
interface DC {
    static void sortLevel(int[][] monstersToSort, int[][] treasuresToSort) {
        // Calculer les valeurs de trésors et de monstres pour chaque rangée
        int numRows = monstersToSort.length;
        int[] rowValues = new int[numRows];
        for (int i = 0; i < numRows; i++) {
            int treasureSum = Arrays.stream(treasuresToSort[i]).filter(value -> value > 0).sum();
            int monsterSum = Arrays.stream(monstersToSort[i]).filter(value -> value < 0).map(Math::abs).sum();
            rowValues[i] = treasureSum - monsterSum;
        }

        // Appliquer le tri fusion en utilisant les valeurs calculées pour chaque rangée
        triFusion(monstersToSort, treasuresToSort, rowValues, 0, numRows - 1);
    }

    static void triFusion(int[][] monsters, int[][] treasures, int[] rowValues, int debut, int fin) {
        if (debut < fin) {
            int milieu = (debut + fin) / 2;
            triFusion(monsters, treasures, rowValues, debut, milieu);
            triFusion(monsters, treasures, rowValues, milieu + 1, fin);
            fusionner(monsters, treasures, rowValues, debut, milieu, fin);
        }
    }

    static void fusionner(int[][] monsters, int[][] treasures, int[] rowValues, int debut, int milieu, int fin) {
        int n1 = milieu - debut + 1;
        int n2 = fin - milieu;
    
        int[][] gaucheMonsters = new int[n1][];
        int[][] gaucheTreasures = new int[n1][];
        int[] gaucheValues = new int[n1];
        int[][] droiteMonsters = new int[n2][];
        int[][] droiteTreasures = new int[n2][];
        int[] droiteValues = new int[n2];
    
        for (int i = 0; i < n1; ++i) {
            gaucheMonsters[i] = monsters[debut + i];
            gaucheTreasures[i] = treasures[debut + i];
            gaucheValues[i] = rowValues[debut + i];
        }
        for (int j = 0; j < n2; ++j) {
            droiteMonsters[j] = monsters[milieu + 1 + j];
            droiteTreasures[j] = treasures[milieu + 1 + j];
            droiteValues[j] = rowValues[milieu + 1 + j];
        }
    
        int i = 0, j = 0;
        int k = debut;
        while (i < n1 && j < n2) {
            // Placer les rangées avec plus de monstres en haut
            if (gaucheValues[i] <= droiteValues[j]) {
                monsters[k] = gaucheMonsters[i];
                treasures[k] = gaucheTreasures[i];
                rowValues[k] = gaucheValues[i];
                i++;
            } else {
                monsters[k] = droiteMonsters[j];
                treasures[k] = droiteTreasures[j];
                rowValues[k] = droiteValues[j];
                j++;
            }
            k++;
        }
    
        // Ajouter les rangées restantes de gauche
        while (i < n1) {
            monsters[k] = gaucheMonsters[i];
            treasures[k] = gaucheTreasures[i];
            rowValues[k] = gaucheValues[i];
            i++;
            k++;
        }
    
        // Ajouter les rangées restantes de droite
        while (j < n2) {
            monsters[k] = droiteMonsters[j];
            treasures[k] = droiteTreasures[j];
            rowValues[k] = droiteValues[j];
            j++;
            k++;
        }
    }
    