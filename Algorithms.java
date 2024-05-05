interface DP {
    static String perfectSolution(State state) {
        int rows = state.monsters.length;
        int cols = state.monsters[0].length;
        ScorePath[][][] dp = new ScorePath[rows][cols][3]; // Troisième dimension pour les directions: 0 = gauche, 1 = droite, 2 = bas

        // Initialisation des chemins possibles avec un score minimal et un chemin vide
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                dp[i][j] = new ScorePath[] {new ScorePath(Integer.MIN_VALUE, ""), new ScorePath(Integer.MIN_VALUE, ""), new ScorePath(Integer.MIN_VALUE, "")};
            }
        }

        // Initialisation de la première ligne où le héros peut choisir de partir de n'importe quelle colonne
        for (int j = 0; j < cols; j++) {
            int initialScore = state.treasures[0][j] - state.monsters[0][j];
            if (state.heroHealth + initialScore > 0) {
                dp[0][j][2] = new ScorePath(initialScore, ""); // Commence ici sans direction horizontale
            }
        }

        // Remplir le tableau dp
        for (int i = 1; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (state.heroHealth > 0) { // Si le héros a encore de la santé
                    updateDP(dp, state, i, j);
                }
            }
        }

        // Trouver le meilleur chemin a partir de la dernière ligne
        ScorePath bestPath = new ScorePath(Integer.MIN_VALUE, "");
        for (int j = 0; j < cols; j++) {
            for (int d = 0; d < 3; d++) {
                if (dp[rows - 1][j][d].score > bestPath.score) {
                    bestPath = dp[rows - 1][j][d];
                }
            }
        }
        return bestPath.path;
    }

    static void updateDP(ScorePath[][][] dp, State state, int i, int j) {
        int rows = state.monsters.length;
        int cols = state.monsters[0].length;

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
        if (j < cols - 1 && dp[i][j+1][1].score != Integer.MIN_VALUE) { // Droite
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
