package pl.edu.agh.kis.lab.pz1;

import lombok.Getter;
import pl.edu.agh.kis.lab.pz1.game_logic.Player;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.THPlayer;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.TexasHoldemGame;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Texas Hold'em game lobby where players can join and a game can be created and started.
 * This class extends the Lobby class and manages a list of players and a TexasHoldemGame instance.
 */
@Getter
public class TexasHoldemLobby extends Lobby {

    /**
     * Initializes a new TexasHoldemLobby with an empty list of players and sets the game as not started.
     */
    public TexasHoldemLobby() {
        players = new ArrayList<>();
        started = false;
    }

    /**
     * Adds a player to the lobby. The player is cast to a Player before being added to the player list.
     *
     * @param player the player to be added to the lobby
     */
    @Override
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Creates a new TexasHoldemGame with the players in the lobby and starts the game.
     * Also sets the game status as started.
     */
    @Override
    public void createGame() {
        game = new TexasHoldemGame((List<THPlayer>)(List<?>) players, 100, 5);
        game.startGame();
        started = true;
    }

    @Override
    public String getGameName(){
        return TexasHoldemGame.class.getSimpleName();
    }

    @Override
    public Player getNewPLayerInstance(){
        return new THPlayer();
    }
}
