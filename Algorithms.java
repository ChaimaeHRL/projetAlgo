import java.util.Random;

public interface Algorithms {
    Random rng = new Random();

    static void main(String[] args) {
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];

        // Appel des différents méthodes utilisées
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
                // Definir des cases vides
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

