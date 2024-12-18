package pl.edu.agh.kis.lab.pz1.game_logic;


public abstract class Game {
    public abstract void startGame();
    public abstract String getName();
    public abstract boolean executeCommand(Player player, String command);
    public abstract String getGameState(String name);
}
