package pl.edu.agh.kis.lab.pz1;

import lombok.Getter;
import pl.edu.agh.kis.lab.pz1.game_logic.Player;
import pl.edu.agh.kis.lab.pz1.game_logic.texas.TexasHoldemGame;

import java.util.List;

/**
 * Represents a lobby for a game, where players can join and games can be created.
 * This abstract class serves as a blueprint for specific types of lobbies.
 */
@Getter
public abstract class Lobby {

    /** A list of players currently in the lobby. */
    List<Player> players;
    /** The Texas Hold'em game instance associated with the lobby. */
    TexasHoldemGame game;
    /** A flag indicating whether the game has started. */
    protected boolean started;
    /**
     * Creates a new game in the lobby. This method must be implemented by subclasses
     * to define the process of creating a game.
     */
    public abstract void createGame();

    /**
     * Adds a player to the lobby. This method must be implemented by subclasses
     * to define how players are added to the lobby.
     *
     * @param player the player to be added to the lobby
     */
    public abstract void addPlayer(Player player);

    public abstract String getGameName();

    public abstract Player getNewPLayerInstance();
}
