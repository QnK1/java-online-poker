package pl.edu.agh.kis.lab.pz1.game_logic;

/**
 * Represents a general game. This is an abstract class that serves as a blueprint
 * for specific types of games. It defines the basic structure of a game by
 * including methods for starting the game, retrieving the game name, executing commands,
 * and getting the current game state.
 */
public abstract class Game {

    /**
     * Starts the game. This method must be implemented by subclasses to define how the game begins.
     */
    public abstract void startGame();

    /**
     * Executes a command from a player. This method must be implemented by subclasses
     * to define how commands are processed during the game.
     *
     * @param p The player executing the command.
     * @param command The command to be executed.
     * @return true if the command is successfully executed, false otherwise.
     */
    public abstract boolean executeCommand(Player p, String command);

    /**
     * Retrieves the current state of the game, usually in a format suitable for display.
     *
     * @param name The name of the player for whom the state is being retrieved.
     * @return A string representing the current state of the game.
     */
    public abstract String getGameState(String name);
}
