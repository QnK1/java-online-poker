package pl.edu.agh.kis.lab.pz1.game_logic;


import pl.edu.agh.kis.lab.pz1.game_logic.texas.THPlayer;

import java.util.List;

public abstract class Game {
    private List<String> lastWinnerNames;
    private int lastWin;
    private List<String> lastWinningCards;

    public abstract void startGame();
    public abstract String getName();
    public abstract boolean executeCommand(Player p, String command);
    public abstract String getGameState(String name);
}
