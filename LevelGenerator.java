/* --- Divide & Conquer --- */
interface DC {
    static void sortLevel(int[][] monstersToSort, int[][] treasuresToSort) {
        int[] rowValues = new int[monstersToSort.length];
        for (int i = 0; i < monstersToSort.length; i++) {
            rowValues[i] = calculateRowValue(monstersToSort[i], treasuresToSort[i]);
        }
        mergeSort(monstersToSort, treasuresToSort, rowValues, 0, monstersToSort.length - 1);
    }

    static int calculateRowValue(int[] monsters, int[] treasures) {
        int totalTreasures = Arrays.stream(treasures).sum();
        int totalMonsterStrengths = Arrays.stream(monsters).sum();
        return totalTreasures - totalMonsterStrengths;
    }

    static void mergeSort(int[][] monsters, int[][] treasures, int[] rowValues, int low, int high) {
        if (low < high) {
            int mid = (low + high) / 2;
            mergeSort(monsters, treasures, rowValues, low, mid);
            mergeSort(monsters, treasures, rowValues, mid + 1, high);
            merge(monsters, treasures, rowValues, low, mid, high);
        }
    }

    static void merge(int[][] monsters, int[][] treasures, int[] rowValues, int low, int mid, int high) {
        int n1 = mid - low + 1;
        int n2 = high - mid;

        int[][] leftMonsters = new int[n1][];
        int[][] leftTreasures = new int[n1][];
        int[] leftRowValues = new int[n1];

        int[][] rightMonsters = new int[n2][];
        int[][] rightTreasures = new int[n2][];
        int[] rightRowValues = new int[n2];

        for (int i = 0; i < n1; ++i) {
            leftMonsters[i] = monsters[low + i];
            leftTreasures[i] = treasures[low + i];
            leftRowValues[i] = rowValues[low + i];
        }
        for (int j = 0; j < n2; ++j) {
            rightMonsters[j] = monsters[mid + 1 + j];
            rightTreasures[j] = treasures[mid + 1 + j];
            rightRowValues[j] = rowValues[mid + 1 + j];
        }

        int i = 0, j = 0, k = low;
        while (i < n1 && j < n2) {
            if (leftRowValues[i] <= rightRowValues[j]) {
                monsters[k] = leftMonsters[i];
                treasures[k] = leftTreasures[i];
                rowValues[k] = leftRowValues[i];
                i++;
            } else {
                monsters[k] = rightMonsters[j];
                treasures[k] = rightTreasures[j];
                rowValues[k] = rightRowValues[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            monsters[k] = leftMonsters[i];
            treasures[k] = leftTreasures[i];
            rowValues[k] = leftRowValues[i];
            i++;
            k++;
        }

        while (j < n2) {
            monsters[k] = rightMonsters[j];
            treasures[k] = rightTreasures[j];
            rowValues[k] = rightRowValues[j];
            j++;
            k++;
        }
    }
}
