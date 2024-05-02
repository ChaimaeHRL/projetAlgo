// méthode naive simple 
//public int pathsCount ( int i , int j ) {
//if ( i==0 || j==0 ) {
//return 1 ;
//} else {
//return pathsCount ( i−1 , j ) + pathsCount ( i , j−1 ) + pathsCount (i−1,j-1);}

//programation_dynamique
public int chemincount(int i, int j, int gridSize) {
    int[][] chemin = new int[gridSize][gridSize];
    // Move null check after initialization
    if (chemin != null) {
        return j;
    }
    for (int x = 0; x < chemin.length; x++) {
        chemin[x][0] = 1;
        chemin[0][x] = 1;
    }
    for (int x = 1; x < chemin.length; x++) {
        for (int y = 1; y < chemin[x].length; y++) {
            chemin[x][y] = chemin[x - 1][y] + chemin[x][y - 1] + chemin[x][y];
        }
    }
    return chemin[i][j];
}
public int getSquareValue(int x, int y) {
    // Supposons que grid soit votre grille représentée par une matrice d'entiers
    // Assurez-vous que x et y sont des indices valides pour votre grille
    return grid[x][y];
}

private void memoizeBestPaths(int gridSize) {
    int[][] score;
    Direction[][] directions;
    score = new int[gridSize][gridSize];
    directions = new String[gridSize][gridSize];
    score[0][0] = getSquareValue(0, 0);
    for (int i = 1; i < gridSize; i++) {
        score[i][0] = score[i - 1][0] + getSquareValue(i, 0);
        directions[i][0] = Direction.RIGHT; // Change Direction.Right to Direction.RIGHT
    }
    for (int j = 1; j < gridSize; j++) {
        score[0][j] = score[0][j - 1] + getSquareValue(0, j);
        directions[0][j] = Direction.UP; // Change Direction.Up to Direction.UP
    }
    for (int i = 1; i < gridSize; i++) {
        for (int j = 1; j < gridSize; j++) {
            int left = score[i - 1][j] + getSquareValue(i, j);
            int bottom = score[i][j - 1] + getSquareValue(i, j);
            int diag = score[i - 1][j - 1] + getSquareValue(i, j); // Fix '-' character
            if (left >= bottom && left >= diag) {
                score[i][j] = left;
                directions[i][j] = Direction.RIGHT;
            } else if (bottom >= left && bottom >= diag) {
                score[i][j] = bottom;
                directions[i][j] = Direction.UP;
            } else {
                score[i][j] = diag;
                directions[i][j] = Direction.DIAGONALLY;
            }
        }
    }
}


