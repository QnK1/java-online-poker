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

    /** A list of players currently in the lobby. */
    final List<THPlayer> players;

    /** The Texas Hold'em game instance associated with the lobby. */
    TexasHoldemGame game;

    /** A flag indicating whether the game has started. */
    private boolean started;

    /**
     * Initializes a new TexasHoldemLobby with an empty list of players and sets the game as not started.
     */
    public TexasHoldemLobby() {
        players = new ArrayList<>();
        started = false;
    }

    /**
     * Adds a player to the lobby. The player is cast to a THPlayer before being added to the player list.
     *
     * @param player the player to be added to the lobby
     */
    @Override
    public void addPlayer(Player player) {
        THPlayer p = (THPlayer) player;
        players.add(p);
    }

    /**
     * Creates a new TexasHoldemGame with the players in the lobby and starts the game.
     * Also sets the game status as started.
     */
    @Override
    public void createGame() {
        game = new TexasHoldemGame(players, 100, 5);
        game.startGame();
        started = true;
    }
}
