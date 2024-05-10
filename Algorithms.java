import java.util.Arrays;
import java.util.Random;

public interface Algorithms {
    Random rng = new Random();

    static void main(String[] args) {
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];
        int[] heroPos = {0, 4}; // Position initiale du héros
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
        int greatSolution = GS.findGreedySolution(state, nbHint, nbLevel, nbHint);
        System.out.println("greatSolution");
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
            return findGreedySolution(state, state.heroPos[0], state.heroPos[1], 5);
        }

        static int findGreedySolution(State state, int row, int col, int remainingDepth) {
            // Base case: depth limit reached or last row reached
            if (remainingDepth == 0 || row >= state.monsters.length) {
                return state.heroHealth + state.treasures[row][col] - state.monsters[row][col];
            }

            int rows = state.monsters.length;
            int cols = state.monsters[0].length;

            // Initialize possible paths
            int scoreLeft = Integer.MIN_VALUE;
            int scoreRight = Integer.MIN_VALUE;
            int scoreDown = Integer.MIN_VALUE;

            // Explore left path
            if (col - 1 >= 0) {
                scoreLeft = findGreedySolution(state, row + 1, col - 1, remainingDepth - 1);
            }

            // Explore right path
            if (col + 1 < cols) {
                scoreRight = findGreedySolution(state, row + 1, col + 1, remainingDepth - 1);
            }

            // Explore down path
            if (row + 1 < rows) {
                scoreDown = findGreedySolution(state, row + 1, col, remainingDepth - 1);
            }

            // Calculate scores with current state
            int currentScore = state.heroHealth + state.treasures[row][col] - state.monsters[row][col];

            // Choose the best path
            int bestScore = Math.max(Math.max(scoreLeft, scoreRight), scoreDown);

            return currentScore + bestScore;
        }
    }

/* --- Dynamic Programming --- */
    
interface DP {
    /**
    * @requires state != null && state.monsters != null && state.treasures != null &&
    * @requires state.monsters.length == state.treasures.length && state.monsters[0].length == state.treasures[0].length &&
    * @requires state.monsters.length > 0 && state.monsters[0].length > 0;
    * @ensures \result != null;
    * @pure
    */
    static String perfectSolution(State state) {
        int lines = state.monsters.length;
        int columns = state.monsters[0].length;
        ScorePath[][][] dp = new ScorePath[lines][columns][3]; 

       //@ loop_invariant 0 <= i && i <= lines;
       //@ loop_invariant (\forall int k, l, m; 0 <= k < i && 0 <= l < columns && 0 <= m < 3; dp[k][l][m] != null);
       //@ decreases lines - i;
        for (int i = 0; i < lines; i++) {
            //@ loop_invariant 0 <= j && j <= columns;
            //@ decreases columns - j;
            for (int j = 0; j < columns; j++) {
                dp[i][j] = new ScorePath[] {
                    new ScorePath(Integer.MIN_VALUE, ""),
                    new ScorePath(Integer.MIN_VALUE, ""),
                    new ScorePath(Integer.MIN_VALUE, "")
                };
            }
        }

        //@ loop_invariant 0 <= j && j <= columns;
        //@ decreases columns - j;
        for (int j = 0; j < columns; j++) {
            int score_initial = state.treasures[0][j] * 2 - state.monsters[0][j] * 5; 
            if (state.heroHealth + score_initial > 0) {
                dp[0][j][0] = new ScorePath(score_initial, "l");
                dp[0][j][1] = new ScorePath(score_initial, "r");
                dp[0][j][2] = new ScorePath(score_initial, "d");
            }
        }

        //@ loop_invariant 1 <= i && i <= lines;
        //@ loop_invariant (\forall int k, l, m; 0 <= k < i && 0 <= l < columns && 0 <= m < 3; dp[k][l][m] != null);
        //@ decreases lines - i;
        for (int i = 1; i < lines; i++) {
            //@ loop_invariant 0 <= j && j <= columns;
            //@ decreases columns - j;
            for (int j = 0; j < columns; j++) {
                updateDP(dp, state, i, j);
            }
        }
        return extractBestPath(dp, lines, columns);
    }
/**
* @requires dp != null && state != null; 
* @requires dp.length > i && i >= 1; // i doit être dans les limites valides de dp et non la première ligne
* @requires dp[0].length > j && j >= 0; // j doit être dans les limites valides de dp
* @requires (\forall int x; 0 <= x < dp.length; dp[x] != null); // vérifie que chaque ligne n'est pas null
* @requires (\forall int x; 0 <= x < dp.length; \forall int y; 0 <= y < dp[x].length; dp[x][y] != null); // vérifie que chaque cellule n'est pas null
* @requires (\forall int x; 0 <= x < dp.length; \forall int y; 0 <= y < dp[x].length; dp[x][y].length == 3); // vérifie que chaque cellule doit avoir 3 directions
* @ensures (\forall int k; 0 <= k < 3; dp[i][j][k].score >= \old(dp[i][j][k].score)); // vérifier que le score ne diminuer pas
* @ensures (\forall int k; 0 <= k < 3; dp[i][j][k].path != null); // vérifier que les paths updates ne sont pas null
*/
    static void updateDP(ScorePath[][][] dp, State state, int i, int j) {
        int columns = state.monsters[0].length;
        for (int prevDir = 0; prevDir < 3; prevDir++) { 
            int prevRow = i - 1;
            for (int offset = -1; offset <= 1; offset++) {
                int prevCol = j + offset; 
                if (prevCol >= 0 && prevCol < columns && dp[prevRow][prevCol][prevDir].score != Integer.MIN_VALUE && !isOppositeDirection(prevDir, offset)) {
                    int score = dp[prevRow][prevCol][prevDir].score + state.treasures[i][j] * 2 - state.monsters[i][j] * 5;
                    int newDir = getNewDirection(offset);
                    if (score > dp[i][j][newDir].score) {
                        dp[i][j][newDir] = new ScorePath(score, dp[prevRow][prevCol][prevDir].path + getDirectionChar(newDir));
                    }
                }
            }
        }
    }

    /**
    * @requires dp != null && lines > 0 && columns > 0 &&
    *  (\forall int i; 0 <= i < lines; 
    *  \forall int j; 0 <= j < columns; 
    *  \forall int d; 0 <= d < 3; dp[i][j][d] != null);
    * @ensures \result != null;
    * @pure
    */
    static String extractBestPath(ScorePath[][][] dp, int lines, int columns) {
        ScorePath bestPath = new ScorePath(Integer.MIN_VALUE, "");
        //@ loop_invariant 0 <= j && j <= columns;
        //@ decreases columns - j;
        for (int j = 0; j < columns; j++) {
            //@ loop_invariant 0 <= d && d < 3;
            //@ decreases 3 - d;
            for (int d = 0; d < 3; d++) {
                if (dp[lines - 1][j][d].score > bestPath.score) {
                    bestPath = dp[lines - 1][j][d];
                }
            }
        }
        return bestPath.path;
    }
    
    static boolean isOppositeDirection(int prevDir, int offset) {
        return (prevDir == 0 && offset == 1) || (prevDir == 1 && offset == -1); 
    }

    static int getNewDirection(int offset) {
        return (offset == -1) ? 0 : (offset == 1) ? 1 : 2; 
    }
    
    static char getDirectionChar(int dir) {
        return (dir == 0) ? 'l' : (dir == 1) ? 'r' : (dir == 2) ? 'd': '\0'; 
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

