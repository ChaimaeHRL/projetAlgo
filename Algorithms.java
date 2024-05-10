
    

import java.lang.Thread.State;
import java.util.Arrays;
import java.util.Random;

public interface Algorithms {
    Random rng = new Random();

    static void main(String[] args) {
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];
        int[] heroPos = {0, 3}; // Position initiale du héros
        int heroHealth = 100; // Santé initiale du héros
        int heroScore = 0; // Score initial du héros
        int nbHint = 0; // Nombre d'indices disponibles
        int nbLevel = 5; // Nombre de niveaux

        State state = new State(heroPos, heroHealth, heroScore, monsters, treasures, nbHint, nbLevel);


        // Appel des différentes méthodes utilisées
        generateMonstersAndTreasures(monsters, treasures);
        verificate(monsters, treasures);
        int[][] board = mix(monsters, treasures);
        System.out.println("Final Board:");
        showBoard(board);
        //int[] heroPos = {0, board[0].length / 2}; // Par exemple, la position initiale du héros
        //State state= new state(heroPos,heroHealth, heroScore,  monsters,  treasures,  nbHint,  nbLevel); 
        
        //Utilisation de la programmation dynamique (DP)
        String bestPath = DP.perfectSolution(state);
        System.out.println("Le chemin optimal est : " + bestPath);
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
            int lines = state.monsters.length;
            int columns = state.monsters[0].length;
            ScorePath[][][] dp = new ScorePath[lines][columns][3]; // Troisième dimension pour les directions: 0 = gauche, 1 = droite, 2 = bas
    
            // Initialisation des chemins possibles avec un score minimal et un chemin vide
            for (int i = 0; i < lines; i++) {
                for (int j = 0; j < columns; j++) {
                    dp[i][j] = new ScorePath[] {new ScorePath(Integer.MIN_VALUE, ""), new ScorePath(Integer.MIN_VALUE, ""), new ScorePath(Integer.MIN_VALUE, "")};
                }
            }
    
            // Initialisation de la première ligne où le héros peut choisir de partir de n'importe quelle colonne
            for (int j = 0; j < columns; j++) {
                int initialScore = state.treasures[0][j] - state.monsters[0][j];
                if (state.heroHealth + initialScore > 0) {
                    dp[0][j][2] = new ScorePath(initialScore, ""); // Commence ici sans direction horizontale
                }
            }
    
            // Remplir le tableau dp
            for (int i = 1; i < lines; i++) {
                for (int j = 0; j < columns; j++) {
                    if (state.heroHealth > 0) { // Si le héros a encore de la santé
                        updateDP(dp, state, i, j);
                    }
                }
            }
    
            // Trouver le meilleur chemin a partir de la dernière ligne
            ScorePath bestPath = new ScorePath(Integer.MIN_VALUE, "");
            for (int j = 0; j < columns; j++) {
                for (int d = 0; d < 3; d++) {
                    if (dp[lines - 1][j][d].score > bestPath.score) {
                        bestPath = dp[lines - 1][j][d];
                    }
                }
            }
            //System.out.println("Le chemin optimal est: " + bestPath.path);
            return bestPath.path;
        }
    
        static void updateDP(ScorePath[][][] dp, State state, int i, int j) {
            int columns = state.monsters[0].length;
    
            for (int dir = 0; dir < 3; dir++) {
                if (dp[i-1][j][dir].score != Integer.MIN_VALUE) { // Héritage du score de l'étage précédent
                    int score = dp[i-1][j][dir].score + state.treasures[i][j] - state.monsters[i][j];
                    if (score > 0) { // Continuation vers le bas si possible
                        dp[i][j][2] = new ScorePath(score, dp[i-1][j][dir].path + "d");
                    }
                }
            }
    
            // Continuation vers la gauche ou la droite
            if (j > 0 && dp[i][j-1][0].score != Integer.MIN_VALUE) { // Gauche
                int leftScore = dp[i][j-1][0].score + state.treasures[i][j] - state.monsters[i][j];
                if (leftScore > 0) {
                    dp[i][j][0] = new ScorePath(leftScore, dp[i][j-1][0].path + "l");
                }
            }
            if (j < columns - 1 && dp[i][j+1][1].score != Integer.MIN_VALUE) { // Droite
                int rightScore = dp[i][j+1][1].score + state.treasures[i][j] - state.monsters[i][j];
                if (rightScore > 0) {
                    dp[i][j][1] = new ScorePath(rightScore, dp[i][j+1][1].path + "r");
                }
            }
        }
    
        class ScorePath {
            int score;
            String path;
    
            ScorePath(int score, String path) {
                this.score = score;
                this.path = path;
            }
        }
    } 

