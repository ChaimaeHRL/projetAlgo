import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmsTest {

    // Generate & Test
    @Test
    void generateTestValuesTest(){
        //arrange
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];

        //act
        Algorithms.GT.generateMonstersAndTreasures(monsters, treasures);

        //assert
        for (int i = 0; i < monsters.length; i++){
            for (int j = 0; j < monsters[i].length; j++){
                assertTrue(treasures[i][j] <= 99, "Treasure value is too high");
                assertTrue(treasures[i][j] >= 0, "Treasure value is too low");
                assertTrue(monsters[i][j] <= 50, "Monster value is too high");
                assertTrue(monsters[i][j] >= 0, "Monster value is too low");
            }
        }
    }

    @Test
    void generateTestNumberPerRowTest(){
        //arrange
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];

        //act
        Algorithms.GT.generateMonstersAndTreasures(monsters, treasures);

        //assert
        for (int i = 0; i < monsters.length; i++){
            int numberTreasures = 0;
            int numberMonsters = 0;
            for (int j = 0; j < monsters[i].length; j++){
                if (treasures[i][j] > 0){numberTreasures++;}
                if (monsters[i][j] > 0){numberMonsters++;}
            }
            assertTrue(numberMonsters >= 2, "Not enough monsters in row");
            assertTrue(numberTreasures <= 2, "Too many treasures in row");
        }
    }

    @Test
    void generateTestTotalValueTest(){
        //arrange
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];

        //act
        Algorithms.GT.generateMonstersAndTreasures(monsters, treasures);

        //assert
        for (int i = 0; i < monsters.length; i++){
            int sumTreasures = 0;
            int sumMonsters = 0;
            for (int j = 0; j < monsters[i].length; j++){
                sumMonsters += monsters[i][j];
                sumTreasures += treasures[i][j];
            }
            assertTrue(sumMonsters >= sumTreasures, "Total value of treasures higher than total value of monsters");
        }
    }

    @Test
    void generateTestSameCellTest(){
        //arrange
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];

        //act
        Algorithms.GT.generateMonstersAndTreasures(monsters, treasures);

        //assert
        for (int i = 0; i < monsters.length; i++){
            for (int j = 0; j < monsters[i].length; j++){
                boolean isMonster = monsters[i][j] > 0;
                boolean isTreasure = treasures[i][j] > 0;
                // Logical XOR
                if (isMonster || isTreasure)
                    assertFalse(isTreasure && isMonster, "Both a monster and a treasure present on a same cell");
            }
        }
    }

    // Divide and Conquer
    @Test
    void divideConquerSortedTest(){
        //arrange
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];
        Algorithms.GT.generateMonstersAndTreasures(monsters, treasures);

        //act
        Algorithms.DC.sortLevel(monsters, treasures);

        //assert
        int previousRow = Integer.MIN_VALUE;
        for (int i = 0; i < monsters.length; i++){
            int rowValue = 0;
            for (int j = 0; j < monsters[i].length; j++){
                rowValue += treasures[i][j] - monsters[i][j];
            }
            assertTrue(previousRow <= rowValue, "Rows are not sorted in increasing order");
            previousRow = rowValue;
        }
    }

    // Dynamic Programming
    @Test
    void dynamicProgrammingValidPathTest(){
        //arrange
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];
        State state = new State(new int[]{0, 3}, 100, 0, monsters, treasures, 1, 1);
        Algorithms.GT.generateMonstersAndTreasures(state.monsters, state.treasures);
        Algorithms.DC.sortLevel(state.monsters, state.treasures);

        //act
        String path = Algorithms.DP.perfectSolution(state);

        //assert
        for (int i = 0; i < path.length(); i++){
            String move = path.substring(i, i + 1);
            switch (move){
                case "l": state.heroPos[1]--;break;
                case "r": state.heroPos[1]++;break;
                case "d": state.heroPos[0]++;break;
            }
            assertTrue(state.heroPos[0] >= 0, "Invalid move");
            assertTrue(state.heroPos[0] < state.monsters.length+1, "Invalid down move");
            assertTrue(state.heroPos[1] >= 0, "Invalid left move");
            assertTrue(state.heroPos[1] < state.monsters[0].length, "Invalid right move");
        }
    }

    @Test
    void dynamicProgrammingValidLastMoveTest(){
        //arrange
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];
        State state = new State(new int[]{0, 3}, 100, 0, monsters, treasures, 1, 1);
        Algorithms.GT.generateMonstersAndTreasures(state.monsters, state.treasures);
        Algorithms.DC.sortLevel(state.monsters, state.treasures);
        String path = Algorithms.DP.perfectSolution(state);
        visitPath(state, path);

        //assert
        assertEquals(state.monsters.length, state.heroPos[0], "Hero did not finish beyond last row");
    }

    @Test
    void dynamicProgrammingAliveHeroTest(){
        //arrange
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];
        State state = new State(new int[]{0, 3}, 100, 0, monsters, treasures, 1, 1);
        Algorithms.GT.generateMonstersAndTreasures(state.monsters, state.treasures);
        Algorithms.DC.sortLevel(state.monsters, state.treasures);
        String path = Algorithms.DP.perfectSolution(state);
        visitPath(state, path);

        //assert
        assertTrue(state.heroHealth > 0, "Hero died");
    }

    @Test
    void dynamicProgrammingValidLeftRightMoveTest(){
        //arrange
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];
        State state = new State(new int[]{0, 3}, 100, 0, monsters, treasures, 1, 1);
        Algorithms.GT.generateMonstersAndTreasures(state.monsters, state.treasures);
        Algorithms.DC.sortLevel(state.monsters, state.treasures);

        //act
        String path = Algorithms.DP.perfectSolution(state);

        //assert
        String prevMove = "d";
        for (int i = 0; i < path.length(); i++){
            String move = path.substring(i, i + 1);
            if (move.equals("l")){assertNotEquals("r", prevMove);}
            if (move.equals("r")){assertNotEquals("l", prevMove);}
            prevMove = move;
        }
    }

    // Greedy Search
    @Test
    void greedySearchSolutionTest(){
        //arrange
        int[][] monsters = new int[11][7];
        int[][] treasures = new int[11][7];
        Algorithms.GT.generateMonstersAndTreasures(monsters, treasures);
        Algorithms.DC.sortLevel(monsters, treasures);
        State state = new State(new int[]{0, 3}, 100, 0, monsters, treasures, 1, 1);
        State bestSolutionState = new State(new int[]{0, 3}, 100, 0, monsters, treasures, 1, 1);

        //act
        String bestPath = Algorithms.DP.perfectSolution(bestSolutionState);
        visitPath(bestSolutionState, bestPath);
        int greedyScore = Algorithms.GS.greedySolution(state);

        //assert
        assertTrue(greedyScore <= bestSolutionState.heroScore, "Greedy solution is better than DP solution");
    }

    // Utilities
    void visitPath(State state, String path){
        for (int i = 0; i < path.length(); i++) {
            state.heroHealth -= state.monsters[state.heroPos[0]][state.heroPos[1]];
            state.heroScore += state.treasures[state.heroPos[0]][state.heroPos[1]];
            String move = path.substring(i, i + 1);
            switch (move){
                case "l": state.heroPos[1]--;break;
                case "r": state.heroPos[1]++;break;
                case "d": state.heroPos[0]++;break;
            }
        }
    }
}