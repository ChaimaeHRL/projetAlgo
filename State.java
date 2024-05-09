/*
* State object : contains everything related to a game's current state
* */
public class State {
    int[] heroPos;  // Current position of the hero in the level
    int heroHealth;  // Current health of the hero, between 0 and 100
    int heroScore;  // Current score of the hero
    int[][] monsters;  // Monsters in the current level
    int[][] treasures;  // Treasures in the current level
    int nbHint;  // Number of hints available to the player
    int nbLevel;  // Current level number, starting at 1 and going up

    public State(int[] heroPos, int heroHealth, int heroScore, int[][] monsters, int[][] treasures, int nbHint, int nbLevel) {
        this.heroPos = heroPos;
        this.heroHealth = heroHealth;
        this.heroScore = heroScore;
        this.monsters = monsters;
        this.treasures = treasures;
        this.nbHint = nbHint;
        this.nbLevel = nbLevel;
    }
}
