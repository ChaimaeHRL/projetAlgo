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
        int nbLevel = 1; // Nombre de niveaux

        State state = new State(heroPos, heroHealth, heroScore, monsters, treasures, nbHint, nbLevel);

        // Appel des différentes méthodes utilisées
        generateMonstersAndTreasures(monsters, treasures);
        verificate(monsters, treasures);
        int[][] board = mix(monsters, treasures);
        System.out.println("Final Board:");
        showBoard(board);
        String bestPath = DP.perfectSolution(state);
        System.out.println("Le chemin optimal est : " + bestPath);
        int greedyScore = GS.greedySolution(state);
        System.out.println("Le score glouton est : " + greedyScore);
        System.out.println("Health: " + state.heroHealth);
        int totalTreasures = DP.TotalTreasures(board);
        System.out.println("Total des tresors collectés: " + totalTreasures);
        int finalScore = state.heroHealth + totalTreasures;
        System.out.println("Score final = (Health + Total des tresors collectés): " + finalScore);
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
                int randomColumn = rng.nextInt(monsters[i].length);
                monsters[i][randomColumn] = 0;// les cases vides
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
    static void showBoard(int[][] board) {//pour afficher le plateau final
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
            /*@ 
              @ requires state != null;
              @ ensures \result >= 0;
              @*/
            static int greedySolution(State state) {
                return goodPath(state, state.heroPos[0], state.heroPos[1], 5);
            }
    
            /*@
              @ requires state != null;
              @ requires row >= 0 && row < state.monsters.length;
              @ requires col >= 0 && col < state.monsters[0].length;
              @ requires remainingDepth >= 0;
              @ ensures \result >= 0;
              @*/
            static int goodPath(State state, int row, int colum, int neededpath) {
                if (neededpath == 0 || row >= state.monsters.length) {//verifier si la dernière ligne est atteinte
                    return state.treasures[row][colum] - state.monsters[row][colum];
                }
                // Initialisation
                int rows = state.monsters.length;
                int colums = state.monsters[0].length;
                int scoreLeft = Integer.MIN_VALUE;
                int scoreRight = Integer.MIN_VALUE;
                int scoreDown = Integer.MIN_VALUE;
                
                if (colum - 1 >= 0) {// la direction gauche
                    scoreLeft = goodPath(state, row + 1, colum - 1, neededpath - 1);
                }

                if (colum + 1 < colums) {// la direction Droite
                    scoreRight = goodPath(state, row + 1, colum + 1, neededpath - 1);
                }
                
                if (row + 1 < rows) {// la direction du bas
                    scoreDown = goodPath(state, row + 1, colum, neededpath - 1);
                }
    
                int currentScore = state.treasures[row][colum] - state.monsters[row][colum];// le score actuel
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
        ScorePath[][][] Options = new ScorePath[lines][columns][3];// tableau des chemins optimals
    
        // Initialisation de dp avec des valeurs par défaut
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                Options[i][j] = new ScorePath[] {
                    new ScorePath(Integer.MIN_VALUE, ""),
                    new ScorePath(Integer.MIN_VALUE, ""),
                    new ScorePath(Integer.MIN_VALUE, "")
                };
            }
        }
    
        // Calcul du chemin optimal
        for (int j = 0; j < columns; j++) {
            int score_initial = state.treasures[0][j] * 2 - state.monsters[0][j] * 5;
            if (state.heroHealth + score_initial > 0) {
                Options[0][j][0] = new ScorePath(score_initial, "l");
                Options[0][j][1] = new ScorePath(score_initial, "r");
                Options[0][j][2] = new ScorePath(score_initial, "d");
            }
        }
    
        for (int i = 1; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                modifyOptions(Options, state, i, j, lines, columns); // Ajout de lignes et colonnes comme paramètres
            }
        }
        return extractBestPath(Options, lines, columns);
    }
    
    static void modifyOptions(ScorePath[][][] Options, State state, int i, int j, int maxLines, int maxColumns) {
        int columns = state.monsters[0].length;
        for (int prevDir = 0; prevDir < 3; prevDir++) {
            int prevRow = i - 1;
            for (int offset = -1; offset <= 1; offset++) {
                int prevCol = j + offset;
                if (prevCol >= 0 && prevCol < columns && prevRow >= 0 && prevRow < maxLines && Options[prevRow][prevCol][prevDir].score != Integer.MIN_VALUE && !isOppositeDirection(prevDir, offset)) {
                    // Calcul du score en fonction des trésors et des monstres
                    int treasureScore = state.treasures[i][j] * 2;
                    int monsterPenalty = state.monsters[i][j] * 5;
                    
                    // Priorité à la case vide plutôt qu'aux monstres
                    int score = Options[prevRow][prevCol][prevDir].score + treasureScore;
                    if (state.monsters[i][j] > 0) {
                        score -= monsterPenalty;
                    }
    
                    int newDir = getNewDirection(offset);
                    if (score > Options[i][j][newDir].score) {
                        // Vérification des limites du plateau après le déplacement
                        int newRow = prevRow + 1;
                        int newCol = prevCol;
                        if (newDir == 0) { // Déplacement vers la gauche
                            newCol = prevCol - 1;
                        } else if (newDir == 1) { // Déplacement vers la droite
                            newCol = prevCol + 1;
                        }
    
                        if (newRow >= 0 && newRow < maxLines && newCol >= 0 && newCol < maxColumns) {
                            Options[i][j][newDir] = new ScorePath(score, Options[prevRow][prevCol][prevDir].path + getDirectionChar(newDir));
                        }
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
    static String extractBestPath(ScorePath[][][] Options, int lines, int columns) {
        ScorePath bestPath = new ScorePath(Integer.MIN_VALUE, "");
        //@ loop_invariant 0 <= j && j <= columns;
        //@ decreases columns - j;
        for (int j = 0; j < columns; j++) {
            //@ loop_invariant 0 <= d && d < 3;
            //@ decreases 3 - d;
            for (int d = 0; d < 3; d++) {
                if (Options[lines - 1][j][d].score > bestPath.score) {
                    bestPath = Options[lines - 1][j][d];
                }
            }
        }
        return bestPath.path;
    }
/* --- Utility functions --- */

    static int TotalTreasures(int[][] board) {
        int total = 0;
        for (int[] row : board) {
            for (int value : row) {
                if (value > 0 && value <= 50) {
                    total += value;
                }
            }
        }
        return total;
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
