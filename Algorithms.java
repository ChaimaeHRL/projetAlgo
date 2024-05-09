import java.util.*;

public interface Algorithms {
    Random rng = new Random();

    static void main(String[] args) {
        // Code de test ou d'exemple
        System.out.println("Hello world!");
    }

    // Définition des directions possibles pour le mouvement
    enum Direction {
        DOWN, LEFT, RIGHT
    }

    /* --- Generate & Test --- */
    interface GT {
        static void generateMonstersAndTreasures(int[][] monstersToFill, int[][] treasuresToFill) {
            // Implémentation de la génération de monstres et trésors
        }
    }

    /* --- Divide & Conquer --- */
    interface DC {
        static void sortLevel(int[][] monstersToSort, int[][] treasuresToSort) {
            // Implémentation du tri de niveau
        }
    }

    /* --- Greedy Search --- */
    interface GS {
        static Direction chooseBestMove(State state) {
            int bestValue = Integer.MIN_VALUE;
            Direction bestDirection = null;
            int[] heroPos = state.heroPos;

            for (Direction dir : Direction.values()) {
                int[] newPos = move(heroPos, dir);
                if (isValidPosition(newPos, state)) {
                    int pathValue = evaluatePath(state, newPos, 5);
                    if (pathValue > bestValue) {
                        bestValue = pathValue;
                        bestDirection = dir;
                    }
                }
            }
            return bestDirection;
        }

        static int[] move(int[] position, Direction dir) {
            return switch (dir) {
                case DOWN -> new int[]{position[0] + 1, position[1]};
                case LEFT -> new int[]{position[0], position[1] - 1};
                case RIGHT -> new int[]{position[0], position[1] + 1};
                default -> position;
            };
        }

        static boolean isValidPosition(int[] position, State state) {
            return position[0] >= 0 && position[0] < state.monsters.length &&
                   position[1] >= 0 && position[1] < state.monsters[0].length;
        }

        static int evaluatePath(State state, int[] pos, int depth) {
            if (depth == 0 || !isValidPosition(pos, state)) {
                return 0;
            }
            int currentValue = getValueAtPosition(state, pos);
            int maxValue = currentValue;
            for (Direction dir : Direction.values()) {
                int[] newPos = move(pos, dir);
                maxValue = Math.max(maxValue, currentValue + evaluatePath(state, newPos, depth - 1));
            }
            return maxValue;
        }

        static int getValueAtPosition(State state, int[] pos) {
            int row = pos[0];
            int col = pos[1];
            int score = state.treasures[row][col] - state.monsters[row][col];
            return score;
        }
    }

    /* --- Dynamic Programming --- */
    interface DP {
        static String perfectSolution(State state) {
            // Implémentation de la solution parfaite via programmation dynamique
            return "";
        }
    }

    /* --- Common utility functions --- */
    // Utilitaires communs si nécessaire
}
