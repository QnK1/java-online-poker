package pl.edu.agh.kis.lab.pz1.game_logic.texas;

import lombok.Getter;
import lombok.Setter;
import pl.edu.agh.kis.lab.pz1.game_logic.Player;

/**
 * Represents a player in the Texas Hold'em game.
 * This class extends the Player class and adds additional attributes
 * specific to the Texas Hold'em game, including the player's hand,
 * money, current bet, and their name.
 */
@Getter
@Setter
public class THPlayer extends Player {

    /**
     * The hand of the player, consisting of the cards they hold.
     */
    private Hand hand;

    /**
     * The amount of money the player has available to bet.
     */
    private int money;

    /**
     * The current bet placed by the player in the ongoing round.
     */
    private int currentBet;

    /**
     * The name of the player.
     */
    private String name;
}