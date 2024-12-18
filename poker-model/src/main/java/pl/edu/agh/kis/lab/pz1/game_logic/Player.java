package pl.edu.agh.kis.lab.pz1.game_logic;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents a player in the game. This is an abstract class that serves as a
 * base for specific player types. It contains the player's name and provides
 * getter and setter methods for accessing and modifying the player's name.
 */
@Getter
@Setter
public abstract class Player {

    /**
     * The name of the player.
     * This field is used to identify the player in the game.
     */
    private String name;
}
