import java.util.*;

public interface Algorithms {
    Random rng = new Random();
    int[][] monstersToFill = new int[11][7];
    int[][] treasuresToFill = new int[11][7];

    static void main(String[] args) {
        // Initialisation des tableaux avec des cases vides
        initializeBoard(monstersToFill);
        initializeBoard(treasuresToFill);
        // Remplissage des monstres et des trésors
        GT.generateMonstersAndTreasures(monstersToFill, treasuresToFill);
        GT.check(monstersToFill, treasuresToFill); // Appel de la méthode de vérification
        // Fusionner les tables de monstres et de trésors dans un seul board
        int[][] board = BoardMerger.merge(monstersToFill, treasuresToFill);
        System.out.println("final Board");
        for (int[] row : board) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println(); // Nouvelle ligne après chaque rangée
        }
        //showArray("Merged Board:", board);
    }

    /* --- Generate & Test --- */
    interface GT {
        static void generateMonstersAndTreasures(int[][] monstersToFill, int[][] treasuresToFill) {
            for (int i = 0; i < monstersToFill.length; i++) {
                for (int j = 0; j < monstersToFill[i].length; j++) {
                    if (i == 0 && j == monstersToFill[i].length / 2) {
                        // Laisser la case au milieu de la première ligne vide
                        continue;
                    }
                    int randomValue = rng.nextInt(6); // Génère un nombre entre 0 et 5
                    if (randomValue == 0 || randomValue == 1) { // 1 chance sur 3 d'être un monstre
                        monstersToFill[i][j] = rng.nextInt(50) + 1; // +1 pour pousser la plage de nombre vers le haut
                    } else if (randomValue == 2) { // 1 chance sur 6 d'être un trésor
                        treasuresToFill[i][j] = rng.nextInt(99) + 1; // Valeur du trésor entre 1 et 99
                    }
                }
            }
        }

        static void check(int[][] monsters, int[][] treasures) {
            for (int i = 0; i < monsters.length; i++) {
                int monstersCount = 0;
                int treasuresCount = 0;
                int totalMonstersValue = 0;
                int totalTreasuresValue = 0;
                for (int j = 0; j < monsters[i].length; j++) {
                    if (monsters[i][j] != 0) {
                        monstersCount++;
                        totalMonstersValue += monsters[i][j];
                    }
                    if (treasures[i][j] != 0) {
                        treasuresCount++;
                        totalTreasuresValue += treasures[i][j];
                    }
                }
                if (monstersCount < 2) {
                    generateMonstersAndTreasures(monsters, treasures);
                    monstersCount = 0;
                }
                if (treasuresCount > 2 || totalTreasuresValue > totalMonstersValue) {
                    generateMonstersAndTreasures(monsters, treasures);
                    treasuresCount = 0;
                }
            }
        }
    }

    /* --- Utility functions for GT --- */
    static void initializeBoard(int[][] board) {
        // Remplit le tableau avec des cases vides
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = 0;
            }
        }
    }

    /* --- Board Merger --- */
    static class BoardMerger {
        static int[][] merge(int[][] monsters, int[][] treasures) {
            int[][] board = new int[monsters.length][monsters[0].length];
            for (int i = 0; i < monsters.length; i++) {
                for (int j = 0; j < monsters[i].length; j++) {
                    if (monsters[i][j] != 0 && treasures[i][j] != 0) {
                        // Si les deux tableaux ont des valeurs non nulles, choisir une aléatoirement
                        if (rng.nextBoolean()) {
                            board[i][j] = monsters[i][j];
                        } else {
                            board[i][j] = treasures[i][j];
                        }
                    } else if (monsters[i][j] != 0) {
                        board[i][j] = monsters[i][j];
                    } else if (treasures[i][j] != 0) {
                        board[i][j] = treasures[i][j];
                    } else {
                        // Si les deux tableaux ont des valeurs nulles, laisser la case vide
                        board[i][j] = 0;
                    }
                }
            }
            return board;
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

