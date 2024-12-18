package pl.edu.agh.kis.lab.pz1;

import pl.edu.agh.kis.lab.pz1.game_logic.Player;

/**
 * Represents a lobby for a game, where players can join and games can be created.
 * This abstract class serves as a blueprint for specific types of lobbies.
 */
public abstract class Lobby {

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
}
