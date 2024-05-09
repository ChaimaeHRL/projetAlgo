import java.util.Random;

public interface Algorithms {
    Random rng = new Random();

    static void main(String[] args) {
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];

        // Remplissage des monstres et des trésors
        generateMonstersAndTreasures(monsters, treasures);
        // Appel de la méthode de vérification
        check(monsters, treasures);
        // Fusionner les tables de monstres et de trésors dans un seul board
        int[][] board = merge(monsters, treasures);

        // Affichage du tableau final
        System.out.println("Final Board:");
        printBoard(board);
    }

    /* --- Generate & Test --- */
    static void generateMonstersAndTreasures(int[][] monstersToFill, int[][] treasuresToFill) {
        for (int i = 0; i < monstersToFill.length; i++) {
            for (int j = 0; j < monstersToFill[i].length; j++) {
                if (i == 0 && j == monstersToFill[i].length / 2) {
                    // Laisser la case au milieu de la première ligne vide
                    continue;
                }
                int randomNum = rng.nextInt(6) + 1; // Génère un nombre entre 1 et 6
                if (randomNum % 6 == 0) { // 1 chance sur 6 d'être un trésor
                    treasuresToFill[i][j] = rng.nextInt(99) + 1; // Valeur du trésor entre 1 et 99
                } else if (randomNum % 3 == 0) { // 1 chance sur 3 d'être un monstre
                    monstersToFill[i][j] = rng.nextInt(50) + 1; // +1 pour pousser la plage de nombre vers le haut
                } else { // Autrement, la case est vide
                    monstersToFill[i][j] = 0;
                    treasuresToFill[i][j] = 0;
                }
            }
        }
    }

    static void check(int[][] monsters, int[][] treasures) {
        for (int i = 0; i < monsters.length; i++) {
            boolean hasEmptyCell = false;
            for (int j = 0; j < monsters[i].length; j++) {
                if (monsters[i][j] == 0 && treasures[i][j] == 0) {
                    hasEmptyCell = true;
                    break;
                }
            }
            if (!hasEmptyCell) {
                // Si aucune case vide n'est trouvée dans la ligne, définir une case vide aléatoire
                int randomColumn = rng.nextInt(monsters[i].length);
                monsters[i][randomColumn] = 0;
                treasures[i][randomColumn] = 0;
            }
        }
    }

    /* --- Board Merger --- */
    static int[][] merge(int[][] monsters, int[][] treasures) {
        int[][] board = new int[monsters.length][monsters[0].length];
        for (int i = 0; i < monsters.length; i++) {
            for (int j = 0; j < monsters[i].length; j++) {
                if (monsters[i][j] != 0 && treasures[i][j] != 0) {
                    // Si les deux tableaux ont des valeurs non nulles, choisir aléatoirement
                    board[i][j] = rng.nextBoolean() ? monsters[i][j] : treasures[i][j];
                } else {
                    // Sinon, choisir la valeur non nulle ou laisser la case vide
                    board[i][j] = monsters[i][j] != 0 ? monsters[i][j] : treasures[i][j];
                }
            }
        }
        return board;
    }

    /* --- Utility functions --- */
    static void printBoard(int[][] board) {
        for (int[] row : board) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println(); // Nouvelle ligne après chaque rangée
        }
    }
}




    /* --- Divide & Conquer --- */
    interface DC {
        static void sortLevel(int[][] monstersToSort, int[][] treasuresToSort) {
            //TODO
        }

        /* --- Utility functions for DC --- */
        //TODO (if you have any)
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

