package pl.edu.agh.kis.lab.pz1;

import lombok.Getter;
import pl.edu.agh.kis.lab.pz1.game_logic.Game;
import pl.edu.agh.kis.lab.pz1.game_logic.Player;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.THPlayer;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.TexasHoldemGame;

import java.util.List;


public abstract class Lobby {
    List<Player> players;
    Game game;
    private boolean started;
    public abstract void createGame();
    public abstract void addPlayer(Player player);
}
