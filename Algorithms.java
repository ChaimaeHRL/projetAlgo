import java.util.Random;
import java.util.Arrays;

public interface Algorithms {
    Random rng = new Random();

    static void main(String[] args) {
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];

        // Appel des différentes méthodes utilisées
        generateMonstersAndTreasures(monsters, treasures);
        verificate(monsters, treasures);
        int[][] board = mix(monsters, treasures);
        System.out.println("Final Board:");
        showBoard(board);
    }

    /* --- Generate & Test --- */
    static void generateMonstersAndTreasures(int[][] monstersToFill, int[][] treasuresToFill) {
        for (int i = 0; i < monstersToFill.length; i++) {
            for (int j = 0; j < monstersToFill[i].length; j++) {
                if (i == 0 && j == monstersToFill[i].length / 2) {
                    continue;
                }
                int randomNum = rng.nextInt(6) + 1; // 1,2,3,4,5,6
                if (randomNum % 6 == 0) { // 1 chance sur 6
                    treasuresToFill[i][j] = rng.nextInt(99) + 1; // Valeur du trésor 
                } else if (randomNum % 3 == 0) { 
                    monstersToFill[i][j] = rng.nextInt(50) + 1; //valeur du monstre
                } else { 
                    monstersToFill[i][j] = 0;//la case est vide
                    treasuresToFill[i][j] = 0;//la case est vide
                }
            }
        }
    }

    static void verificate(int[][] monsters, int[][] treasures) {
        for (int i = 0; i < monsters.length; i++) {
            boolean Empty = false;
            for (int j = 0; j < monsters[i].length; j++) {
                if (monsters[i][j] == 0 && treasures[i][j] == 0) {
                    Empty = true;
                    break;
                }
            }
            if (!Empty) {
                // Définir des cases vides
                int randomColumn = rng.nextInt(monsters[i].length);
                monsters[i][randomColumn] = 0;
                treasures[i][randomColumn] = 0;
            }
        }
    }
    
    static int[][] mix(int[][] monsters, int[][] treasures) {
        int[][] board = new int[monsters.length][monsters[0].length];
        for (int i = 0; i < monsters.length; i++) {
            for (int j = 0; j < monsters[i].length; j++) {
                if (monsters[i][j] != 0 && treasures[i][j] != 0) {
                    // Si les deux tableaux ont des valeurs non nulles, choisir aléatoirement
                    board[i][j] = rng.nextBoolean() ? monsters[i][j] : treasures[i][j];// si les 2 tables ont des valeurs non nuls on va choisir aléatoirement
                } else {
                    board[i][j] = monsters[i][j] != 0 ? monsters[i][j] : treasures[i][j];
                }
            }
        }
        return board;
    }

    /* --- Utility functions --- */
    static void showBoard(int[][] board) {
        for (int[] row : board) {
            for (int value : row) {
                if (value == 0) {
                    System.out.print("0   "); // Case vide
                } else if (value <= 50) {
                    System.out.printf("%-4s", "M" + value); // Afficher la valeur du monstre
                } else {
                    System.out.printf("%-4s", "T" + (value - 50)); // Afficher la valeur du trésor
                }
            }
            System.out.println(); 
        }
    }
}

/* --- Divide & Conquer --- */

interface DC {
    static void sortLevel(int[][] monstersToSort, int[][] treasuresToSort) {
        int[] rowValues = new int[monstersToSort.length];
        for (int i = 0; i < monstersToSort.length; i++) {
            rowValues[i] = calculateRowValue(monstersToSort[i], treasuresToSort[i]);
        }
        sortByRowValue(monstersToSort, treasuresToSort, rowValues, 0, monstersToSort.length - 1);
    }

    static int calculateRowValue(int[] monsters, int[] treasures) {
        int totalTreasures = Arrays.stream(treasures).sum();
        int totalMonsterStrengths = Arrays.stream(monsters).sum();
        return totalTreasures - totalMonsterStrengths;
    }

    static void sortByRowValue(int[][] monsters, int[][] treasures, int[] rowValues, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            sortByRowValue(monsters, treasures, rowValues, low, mid);
            sortByRowValue(monsters, treasures, rowValues, mid + 1, high);
            merge(monsters, treasures, rowValues, low, mid, high);
        }
    }

    static void merge(int[][] monsters, int[][] treasures, int[] rowValues, int low, int mid, int high) {
        int leftSize = mid - low + 1;
        int rightSize = high - mid;

        // Tableaux temporaires pour stocker les parties gauche et droite
        int[][] leftMonsters = new int[leftSize][];
        int[][] leftTreasures = new int[leftSize][];
        int[] leftValues = new int[leftSize];

        int[][] rightMonsters = new int[rightSize][];
        int[][] rightTreasures = new int[rightSize][];
        int[] rightValues = new int[rightSize];

        // Copie des parties gauche et droite dans les tableaux temporaires
        System.arraycopy(monsters, low, leftMonsters, 0, leftSize);
        System.arraycopy(treasures, low, leftTreasures, 0, leftSize);
        System.arraycopy(rowValues, low, leftValues, 0, leftSize);

        System.arraycopy(monsters, mid + 1, rightMonsters, 0, rightSize);
        System.arraycopy(treasures, mid + 1, rightTreasures, 0, rightSize);
        System.arraycopy(rowValues, mid + 1, rightValues, 0, rightSize);

        int i = 0, j = 0, k = low;
        while (i < leftSize && j < rightSize) {
            // Inverser l'ordre de tri en comparant les valeurs de rowValues
            if (leftValues[i] >= rightValues[j]) {
                monsters[k] = leftMonsters[i];
                treasures[k] = leftTreasures[i];
                rowValues[k] = leftValues[i];
                i++;
            } else {
                monsters[k] = rightMonsters[j];
                treasures[k] = rightTreasures[j];
                rowValues[k] = rightValues[j];
                j++;
            }
            k++;
        }

        // Copie des éléments restants dans les parties gauche et droite
        while (i < leftSize) {
            monsters[k] = leftMonsters[i];
            treasures[k] = leftTreasures[i];
            rowValues[k] = leftValues[i];
            i++;
            k++;
        }

        while (j < rightSize) {
            monsters[k] = rightMonsters[j];
            treasures[k] = rightTreasures[j];
            rowValues[k] = rightValues[j];
            j++;
            k++;
        }
    }
}



    /* --- Greedy Search --- */
    interface GS {
        static int greedySolution(State state) {
            //TODO
            return 0;
        }

        /* --- Utility functions for GS --- */
        //TODO (if you have any)
    }

    /* --- Dynamic Programming --- */
    interface DP {
        static String perfectSolution(State state) {
            //TODO
            return "";
        }

        /* --- Utility functions for DP --- */
        //TODO (if you have any)
    }

    /* --- Common utility functions --- */
    //TODO (if you have any)

